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
import pl.gungnir.fooddecider.model.useCase.GetAllSavedFoodUseCase
import pl.gungnir.fooddecider.model.useCase.SetFoodListUseCase
import pl.gungnir.fooddecider.util.RANDOM_FOOD_TIME
import pl.gungnir.fooddecider.util.onFailure
import pl.gungnir.fooddecider.util.onSuccess
import kotlin.random.Random

class SaveFoodShareViewModel(
    private val getAllSavedFoodUseCase: GetAllSavedFoodUseCase,
    private val setFoodListUseCase: SetFoodListUseCase
) : ViewModel() {

    private val _listOfSavedFood: SnapshotStateList<String> = mutableStateListOf()
    val listOfSavedFood: MutableState<Result> = mutableStateOf(Result.Empty)
    val randomFood: MutableState<Result> = mutableStateOf(Result.Empty)

    val newFood: MutableState<String> = mutableStateOf("")

    fun onInitialize() {
        if (_listOfSavedFood.isEmpty()) {
            viewModelScope.launch {
                getAllSavedFoodUseCase.run("")
                    .onSuccess {
                        it.map {
                            listOfSavedFood.value = Result.Loading
                            _listOfSavedFood.clear()
                            _listOfSavedFood.addAll(it)
                            savedResult(it)
                        }.launchIn(this)
                    }
            }
        }
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
                val index = Random.nextInt(0, _listOfSavedFood.size)
                randomFood.value = Result.Success(_listOfSavedFood[index])
            }

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