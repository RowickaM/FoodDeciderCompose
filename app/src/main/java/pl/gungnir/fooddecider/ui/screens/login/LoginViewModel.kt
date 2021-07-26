package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.useCase.IsUserLoggedUseCase
import pl.gungnir.fooddecider.model.useCase.LoginUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.onFailure
import pl.gungnir.fooddecider.util.onSuccess

class LoginViewModel(
    private val resourceProvider: ResourceProvider,
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase
) : ViewModel() {

    val isUserLogged: MutableState<Boolean?> = mutableStateOf(null)

    fun onInitialize() {
        isUserLogged.value ?: viewModelScope.launch {
            delay(800)
            isUserLoggedUseCase.run(None)
                .onSuccess {
                    isUserLogged.value = it
                }
        }
    }

    fun onLoginClick(
        email: String,
        password: String,
        afterSuccess: () -> Unit,
        afterFailure: (String) -> Unit,
    ) {
        viewModelScope.launch {
            isUserLogged.value = null
            loginUseCase.run(LoginUseCase.Params(email, password))
                .onSuccess {
                    afterSuccess.invoke()
                }.onFailure {
                    val message = when (it) {
                        Failure.UserNotExist -> resourceProvider.getString(R.string.firebase_user_not_exist)
                        Failure.InvalidCredentials -> resourceProvider.getString(R.string.firebase_invalid_credentials)
                        else -> resourceProvider.getString(R.string.firebase_unknown)
                    }
                    afterFailure.invoke(message)
                }
        }
    }
}