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
import kotlin.random.Random

class SaveFoodShareViewModel(
    private val getAllSavedFoodUseCase: GetAllSavedFoodUseCase
) : ViewModel() {

    private val _listOfSavedFood: SnapshotStateList<String> = mutableStateListOf()
    val listOfSavedFood: MutableState<Result> = mutableStateOf(Result.Empty)
    val randomFood: MutableState<Result> = mutableStateOf(Result.Empty)

    fun onInitialize() {
        if (_listOfSavedFood.isEmpty()) {
            getAllSavedFoodUseCase.invoke("")
                .map {
                    listOfSavedFood.value = Result.Loading
                    _listOfSavedFood.clear()
                    _listOfSavedFood.addAll(it)
                    listOfSavedFood.value = Result.SuccessFetch(_listOfSavedFood)
                }
                .launchIn(viewModelScope)
        }
    }

    fun drawFood() {
        if (!_listOfSavedFood.isNullOrEmpty()) {
            viewModelScope.launch {
                randomFood.value = Result.Loading
                delay(1000)
                val index = Random.nextInt(0, _listOfSavedFood.size)
                randomFood.value = Result.Success(_listOfSavedFood[index])
            }

        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: String) : Result()
    class SuccessFetch(val result: List<String>) : Result()
}