package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel(

) : ViewModel() {

    private val login: MutableState<String> = mutableStateOf("")
    private val password: MutableState<String> = mutableStateOf("")

    fun onLoginChange(login: String) {
        this.login.value = login
    }

    fun onPasswordChange(password: String) {
        this.password.value = password
    }

    fun onLoginClick() {

    }
}