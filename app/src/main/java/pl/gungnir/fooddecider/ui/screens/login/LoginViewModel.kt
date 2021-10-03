package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.useCase.*
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.config.Config
import pl.gungnir.fooddecider.util.helper.ResourceProvider

class LoginViewModel(
    private val resourceProvider: ResourceProvider,
    private val config: Config,
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val checkDBVersion: CheckDBVersion,
    private val changeStructureUseCase: ChangeStructureUseCase,
) : ViewModel() {

    val isUserLogged: MutableState<Boolean?> = mutableStateOf(null)
    val showLoader: MutableState<Boolean> = mutableStateOf(false)

    private var uid = ""

    fun onInitialize() {
        isUserLogged.value ?: viewModelScope.launch {
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
        afterFailure: (Boolean, String) -> Unit,
    ) {
        showLoader.value = true
        viewModelScope.launch {
            isUserLogged.value = null
            loginUseCase.run(LoginUseCase.Params(email, password))
                .onSuccess {
                    checkVersion(afterSuccess)
                }.onFailure {
                    showLoader.value = false
                    val message = when (it) {
                        Failure.UserNotExist -> resourceProvider.getString(R.string.firebase_user_not_exist)
                        Failure.InvalidCredentials -> resourceProvider.getString(R.string.firebase_invalid_credentials)
                        is Failure.UserNotVerify -> {
                            uid = it.userUID
                            resourceProvider.getString(R.string.firebase_user_not_verify)
                        }
                        else -> resourceProvider.getString(R.string.firebase_unknown)
                    }
                    isUserLogged.value = false
                    afterFailure.invoke(it is Failure.UserNotVerify, message)
                }
        }
    }

    fun onSendEmailVerification(
        afterSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            sendEmailVerificationUseCase.run(uid)
                .fold({}) {
                    showLoader.value = false
                    isUserLogged.value = false
                    afterSuccess.invoke(resourceProvider.getString(R.string.send_email_verification))
                }
        }
    }

    private fun checkVersion(onSuccess: () -> Unit) {
        viewModelScope.launch {
            checkDBVersion.run(None)
                .onSuccess { isActual ->
                    if (isActual) {
                        showLoader.value = false
                        onSuccess.invoke()
                    } else {
                        changeStructure(onSuccess)
                    }
                }
        }
    }

    private fun changeStructure(onSuccess: () -> Unit) {
        viewModelScope.launch {
            changeStructureUseCase.run(None)
                .onSuccess {
                    showLoader.value = false
                    saveNewDBVersion()
                    onSuccess.invoke()
                }
        }
    }

    private fun saveNewDBVersion() {
        config.databaseVersion = DB_VERSION_IN_APP
    }

}