package pl.gungnir.fooddecider.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.model.useCase.LoginUseCase
import pl.gungnir.fooddecider.util.onSuccess

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun onLoginClick(email: String, password: String, afterSuccess: () -> Unit) {
        viewModelScope.launch {
            loginUseCase.run(LoginUseCase.Params(email, password))
                .onSuccess {
                    afterSuccess.invoke()
                }
        }
    }
}