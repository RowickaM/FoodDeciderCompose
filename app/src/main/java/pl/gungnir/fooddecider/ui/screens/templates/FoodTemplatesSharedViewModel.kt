package pl.gungnir.fooddecider.ui.screens.templates

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.useCase.GetTemplatesUseCase
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.onSuccess

class FoodTemplatesSharedViewModel(
    private val getTemplatesUseCase: GetTemplatesUseCase,
) : ViewModel() {

    val templates: MutableState<Result?> = mutableStateOf(null)

    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)

    fun onInitialize() {
        if (templates.value == null) {
            fetchData()
        }
    }

    private fun fetchData() {
        templates.value = Result.Loading
        viewModelScope.launch {
            getTemplatesUseCase.run(None)
                .onSuccess {
                    if (it.isEmpty()) {
                        templates.value = Result.Empty
                    } else {
                        templates.value = Result.Success(it)
                    }
                }
        }
    }

    fun onRefresh(delay: Long = 500) {
        isRefreshing.value = true
        viewModelScope.launch {
            templates.value = Result.Loading
            delay(delay)
            fetchData()
        }
        isRefreshing.value = false
    }

    @VisibleForTesting
    fun changeList(list: List<Template>) {
        if (list.isEmpty()) {
            templates.value = Result.Empty
        } else {
            templates.value = Result.Success(list)
        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<Template>) : Result()
}