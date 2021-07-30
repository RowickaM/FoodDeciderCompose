package pl.gungnir.fooddecider.ui.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.useCase.SendRemindPasswordLinkUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.helper.ResourceProvider

class ForgotPasswordViewModel(
    private val resourceProvider: ResourceProvider,
    private val sendRemindPasswordLinkUseCase: SendRemindPasswordLinkUseCase
) : ViewModel() {

    fun sendLink(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            sendRemindPasswordLinkUseCase.run(email)
                .fold(
                    {
                        val message = when (it) {
                            Failure.UserNotExist -> resourceProvider.getString(R.string.firebase_user_not_exist)
                            else -> resourceProvider.getString(R.string.firebase_unknown)
                        }
                        onFailure("")
                    }, { onSuccess() })
        }
    }
}