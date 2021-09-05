package pl.gungnir.fooddecider.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.useCase.CreateUserCollectionUseCase
import pl.gungnir.fooddecider.model.useCase.LogoutUseCase
import pl.gungnir.fooddecider.model.useCase.SendEmailVerificationUseCase
import pl.gungnir.fooddecider.model.useCase.SignUpUserUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.helper.ResourceProvider

class RegistrationViewModel(
    private val resourceProvider: ResourceProvider,
    private val signUpUserUseCase: SignUpUserUseCase,
    private val createUserCollectionUseCase: CreateUserCollectionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
) : ViewModel() {

    fun onRegistrationClick(
        email: String,
        password: String,
        afterSuccess: (String) -> Unit,
        afterFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            signUpUserUseCase.run(SignUpUserUseCase.Params(email, password))
                .fold(
                    {
                        println("fail!!")
                        val message = when (it) {
                            Failure.UserCollision -> resourceProvider.getString(R.string.firebase_user_collision)
                            Failure.FirebaseAuthUnknown -> resourceProvider.getString(R.string.firebase_unknown)
                            else -> resourceProvider.getString(R.string.error_unknown)
                        }

                        afterFailure.invoke(message)
                    },
                    {
                        createUserCollection(it, afterSuccess, afterFailure)
                    }
                )
        }
    }

    private suspend fun createUserCollection(
        userUID: String,
        afterSuccess: (String) -> Unit,
        afterFailure: (String) -> Unit
    ) {
        createUserCollectionUseCase.run(userUID)
            .fold(
                {
                    val message = resourceProvider.getString(R.string.error_unknown)
                    afterFailure.invoke(message)
                },
                {
                    sendEmailVerification(userUID, afterSuccess)
                }
            )
    }

    private suspend fun sendEmailVerification(
        userUID: String,
        afterSuccess: (String) -> Unit,
    ) {
        sendEmailVerificationUseCase.run(userUID)
            .fold({}) {
                logoutUseCase.run(None)
                    .run {
                        afterSuccess.invoke(resourceProvider.getString(R.string.send_email_verification))
                    }
            }

    }
}