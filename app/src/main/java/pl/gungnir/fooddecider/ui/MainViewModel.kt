package pl.gungnir.fooddecider.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.useCase.LogoutUseCase
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.onSuccess

class MainViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun logout(onSuccessLogout: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase.run(None)
                .onSuccess {
                    onSuccessLogout.invoke()
                }
        }
    }
}