package pl.gungnir.fooddecider.screens.randomizeFood

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.data.savedFood
import kotlin.random.Random

class RandomizeFoodViewModel : ViewModel() {

    private val listOfSavedFood: SnapshotStateList<String> = mutableStateListOf()
    val randomFood: MutableState<Result> = mutableStateOf(Result.Empty)

    fun onInitialize() {
        listOfSavedFood.clear()
        listOfSavedFood.addAll(savedFood)
    }

    fun drawFood() {
        if (!listOfSavedFood.isNullOrEmpty()) {
            viewModelScope.launch {
                randomFood.value = Result.Loading
                delay(1000)
                val index = Random.nextInt(0, listOfSavedFood.size)
                randomFood.value = Result.Success(listOfSavedFood[index])
            }

        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: String) : Result()
}