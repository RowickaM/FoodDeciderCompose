package pl.gungnir.fooddecider.ui.screens.randomizeFood

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.useCase.ChangeStructureUseCase
import pl.gungnir.fooddecider.model.useCase.CheckDBVersion
import pl.gungnir.fooddecider.model.useCase.GetSavedItemsCollectionUseCase
import pl.gungnir.fooddecider.model.useCase.SetFoodListUseCase
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.config.Config
import kotlin.random.Random

class SaveFoodShareViewModel(
    private val getSavedItemsCollectionUseCase: GetSavedItemsCollectionUseCase,
    private val setFoodListUseCase: SetFoodListUseCase,
    private val checkDBVersion: CheckDBVersion,
    private val changeStructureUseCase: ChangeStructureUseCase,
    private val config: Config,
) : ViewModel() {

    private val _listOfSavedFood: SnapshotStateList<String> = mutableStateListOf()
    val listOfSavedFood: MutableState<Result> = mutableStateOf(Result.Empty)
    val randomFood: MutableState<Result> = mutableStateOf(Result.Empty)

    val newFood: MutableState<String> = mutableStateOf("")

    private var setSavedList: ((List<String>, String) -> Unit)? = null
    private var listName: MutableState<String> = mutableStateOf("")

    fun onInitialize(setSavedList: (List<String>, String) -> Unit) {
        this.setSavedList = setSavedList

        if (config.listName != listName.value) {
            checkVersion()
        } else if (_listOfSavedFood.isEmpty()) {
            checkVersion()
        }
    }

    private fun checkVersion() {
        viewModelScope.launch {
            checkDBVersion.run(None)
                .onSuccess { isActual ->
                    if (isActual) {
                        getList()
                    } else {
                        changeStructure()
                    }
                }
        }
    }

    private fun getList() {
        viewModelScope.launch {
            getSavedItemsCollectionUseCase.run(config.listName)
                .onSuccess {
                    it.map {
                        listOfSavedFood.value = Result.Loading

                        val savedListItems = it.savedList
                        _listOfSavedFood.clear()
                        _listOfSavedFood.addAll(savedListItems)
                        savedResult(savedListItems)

                        listName.value = it.selectedListName

                        setSavedList?.invoke(
                            it.allListName,
                            it.selectedListName
                        )
                    }.launchIn(this)
                }
        }
    }

    private fun changeStructure() {
        viewModelScope.launch {
            changeStructureUseCase.run(None)
                .onSuccess {
                    saveNewDBVersion()
                    getList()
                }
        }
    }

    private fun saveNewDBVersion() {
        config.databaseVersion = DB_VERSION_IN_APP
    }

    private fun savedResult(list: List<String>) {
        if (list.isEmpty()) {
            listOfSavedFood.value = Result.Empty
        } else {
            listOfSavedFood.value = Result.SuccessFetch(list)
        }
    }

    fun drawFood(delay: Long = RANDOM_FOOD_TIME.toLong()) {
        if (!_listOfSavedFood.isNullOrEmpty()) {
            viewModelScope.launch {
                randomFood.value = Result.Loading
                delay(delay)
                val index = getRandomIndex(_listOfSavedFood)
                randomFood.value = Result.Success(_listOfSavedFood[index])
            }
        }
    }

    private fun getRandomIndex(list: List<String>): Int {
        return if (list.size == 1) {
            0
        } else {
            Random.nextInt(0, _listOfSavedFood.size)
        }
    }

    fun onFoodNameChange(newFoodName: String) {
        newFood.value = newFoodName
    }

    fun onAddFoodClick(onSuccess: () -> Unit) {
        _listOfSavedFood.add(newFood.value)
        viewModelScope.launch {
            setFoodListUseCase.run(_listOfSavedFood)
                .onSuccess {
                    newFood.value = ""
                    onSuccess()
                }
                .onFailure {
                    _listOfSavedFood.remove(newFood.value)
                }
        }
    }

    fun onRemoveFood(foodIndex: Int) {
        _listOfSavedFood.removeAt(foodIndex)
        viewModelScope.launch {
            setFoodListUseCase.run(_listOfSavedFood)
        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: String) : Result()
    class SuccessFetch(val result: List<String>) : Result()
}