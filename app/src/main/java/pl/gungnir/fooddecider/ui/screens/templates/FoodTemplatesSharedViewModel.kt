package pl.gungnir.fooddecider.ui.screens.templates

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
    private val getTemplatesUseCase: GetTemplatesUseCase
) : ViewModel() {

    val templates: MutableState<Result?> = mutableStateOf(null)

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

    fun getTemplateById(templateId: String): Template? {
        return if (templates.value !is Result.Success) {
            null
        } else {
            (templates.value as Result.Success).result.find { it.id == templateId }
        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<Template>) : Result()
}