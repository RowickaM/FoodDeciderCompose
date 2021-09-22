package pl.gungnir.fooddecider.ui.screens.templatesDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.model.useCase.GetTemplateDetailsUseCase
import pl.gungnir.fooddecider.model.useCase.SaveItemToListUseCase
import pl.gungnir.fooddecider.util.onSuccess

class TemplateDetailsViewModel(
    private val getTemplateDetailsUseCase: GetTemplateDetailsUseCase,
    private val saveItemToListUseCase: SaveItemToListUseCase
) : ViewModel() {

    val templateDetails: MutableState<TemplateDetails?> = mutableStateOf(null)

    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    private var templateId: String? = null

    fun getTemplateById(id: String) {
        viewModelScope.launch {
            getTemplateDetailsUseCase.run(id)
                .onSuccess {
                    templateDetails.value = it
                    templateId = id
                }
        }
    }

    fun onRefreshDetails(delay: Long = 500) {
        isRefreshing.value = true
        viewModelScope.launch {
            templateDetails.value = templateDetails.value?.copy(
                added = emptyList(),
                notAdded = emptyList()
            )
            delay(delay)
            templateId?.let { getTemplateById(it) }
        }
        isRefreshing.value = false
    }

    fun onAddButtonClick(foodName: String) {
        viewModelScope.launch {
            saveItemToListUseCase.run(foodName)
                .onSuccess {
                    onRefreshDetails()
                }
        }
    }
}