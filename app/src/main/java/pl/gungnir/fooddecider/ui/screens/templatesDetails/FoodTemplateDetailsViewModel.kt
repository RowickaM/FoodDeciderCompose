package pl.gungnir.fooddecider.ui.screens.templatesDetails

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.model.useCase.AddFoodToListUseCase
import pl.gungnir.fooddecider.model.useCase.SplitDishesTemplateByIdUseCase
import pl.gungnir.fooddecider.util.onSuccess

class FoodTemplateDetailsViewModel(
    private val splitDishesTemplateUseCase: SplitDishesTemplateByIdUseCase,
    private val addFoodToListUseCase: AddFoodToListUseCase,
) : ViewModel() {

    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    private val templateId: MutableState<String> = mutableStateOf("")

    val templateDetails: MutableState<TemplateDetails?> = mutableStateOf(null)

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

    fun getTemplateById(id: String) {
        viewModelScope.launch {
            Log.d("MRMRMR", "getTemplateById: $id")
            splitDishesTemplateUseCase.run(id)
                .onSuccess {
                    Log.d("MRMRMR", "getTemplateById: ${it.first}")
                    templateDetails.value = it.first
                    templateId.value = id
                }
        }
    }

    fun onAddButtonClick(foodName: String) {
        viewModelScope.launch {
            addFoodToListUseCase.run(foodName).onSuccess {
                onRefreshDetails()
            }
        }
    }

    @VisibleForTesting
    fun setTemplateDetails(template: TemplateDetails) {
        Log.d("MRMRMR", "a tu chce dać rzeczy")
        templateDetails.value = template
        templateId.value = template.id
    }
}