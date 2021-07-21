package pl.gungnir.fooddecider.ui.screens.savedFood

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pl.gungnir.fooddecider.model.data.savedFood

class SavedFoodViewModel : ViewModel() {

    val listOfSavedFood: MutableState<Result> = mutableStateOf(Result.Empty)

    fun onInitialize() {
        listOfSavedFood.value = Result.Loading
        listOfSavedFood.value = Result.Success(savedFood)
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<String>) : Result()
}