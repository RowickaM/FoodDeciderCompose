package pl.gungnir.fooddecider.ui.screens.templates

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getTemplateById(templateId: String) {
        val templates = (this.templates.value as Result.Success).result

        templates.find { it.id == templateId }
            ?.let {
                viewModelScope.launch {
                    splitDishesTemplateUseCase.run(it)
                        .onSuccess {
                            templateDetails.value = it
                        }
                }
            }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<Template>) : Result()
}