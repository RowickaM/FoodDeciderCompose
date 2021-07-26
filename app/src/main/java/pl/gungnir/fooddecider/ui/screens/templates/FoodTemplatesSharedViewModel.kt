package pl.gungnir.fooddecider.ui.screens.templates

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.model.useCase.GetTemplatesUseCase
import pl.gungnir.fooddecider.model.useCase.SplitDishesTemplateUseCase
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.onSuccess

class FoodTemplatesSharedViewModel(
    private val getTemplatesUseCase: GetTemplatesUseCase,
    private val splitDishesTemplateUseCase: SplitDishesTemplateUseCase
) : ViewModel() {

    val templates: MutableState<Result?> = mutableStateOf(null)
    val templateDetails: MutableState<TemplateDetails?> = mutableStateOf(null)

    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    private val templateId: MutableState<String> = mutableStateOf("")

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

    fun getTemplateById(id: String) {
        templateId.value = ""
        val templates = (this.templates.value as? Result.Success)?.result

        templates?.find { it.id == id }
            ?.let {
                viewModelScope.launch {
                    splitDishesTemplateUseCase.run(it)
                        .onSuccess {
                            templateDetails.value = it
                            templateId.value = id
                        }
                }
            }
    }

    fun onRefresh() {
        isRefreshing.value = true
        viewModelScope.launch {
            templates.value = Result.Loading
            delay(500)
            fetchData()
        }
        isRefreshing.value = false
    }

    fun onRefreshDetails() {
        isRefreshing.value = true
        viewModelScope.launch {
            templateDetails.value = templateDetails.value?.copy(
                added = emptyList(),
                notAdded = emptyList()
            )
            delay(500)
            getTemplateById(templateId.value)
        }
        isRefreshing.value = false
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<Template>) : Result()
}