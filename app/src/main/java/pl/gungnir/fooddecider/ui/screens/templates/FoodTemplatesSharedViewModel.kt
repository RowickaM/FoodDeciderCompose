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
import pl.gungnir.fooddecider.model.useCase.SetFoodListUseCase
import pl.gungnir.fooddecider.model.useCase.SplitDishesTemplateUseCase
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.onSuccess

class FoodTemplatesSharedViewModel(
    private val getTemplatesUseCase: GetTemplatesUseCase,
    private val splitDishesTemplateUseCase: SplitDishesTemplateUseCase,
    private val setFoodListUseCase: SetFoodListUseCase
) : ViewModel() {

    private val allSavedFood: MutableState<ArrayList<String>> = mutableStateOf(arrayListOf())

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
                            templateDetails.value = it.first
                            templateId.value = id
                            allSavedFood.value.clear()
                            allSavedFood.value.addAll(it.second)
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

    fun onRefreshDetails(delay: Long = 500) {
        isRefreshing.value = true
        viewModelScope.launch {
            templateDetails.value = templateDetails.value?.copy(
                added = emptyList(),
                notAdded = emptyList()
            )
            delay(delay)
            getTemplateById(templateId.value)
        }
        isRefreshing.value = false
    }

    fun onAddButtonClick(foodName: String) {
        val saved = allSavedFood.value
        saved.add(foodName)

        viewModelScope.launch {
            setFoodListUseCase.run(saved).onSuccess {
                val details = templateDetails.value
                val added = arrayListOf<String>()
                val noAdded = arrayListOf<String>()

                added.addAll(details?.added ?: emptyList())
                noAdded.addAll(details?.notAdded ?: emptyList())

                added.add(foodName)
                noAdded.remove(foodName)

                templateDetails.value = details?.copy(
                    added = added,
                    notAdded = noAdded
                )
            }
        }
    }
}

sealed class Result {
    object Empty : Result()
    object Loading : Result()
    class Success(val result: List<Template>) : Result()
}