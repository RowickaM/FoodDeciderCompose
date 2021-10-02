package pl.gungnir.fooddecider.ui.screens.templates

import androidx.annotation.VisibleForTesting
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
