package pl.gungnir.fooddecider.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.useCase.CreateUserCollectionUseCase
import pl.gungnir.fooddecider.model.useCase.SignUpUserUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.helper.ResourceProvider

class RegistrationViewModel(
    private val resourceProvider: ResourceProvider,
    private val signUpUserUseCase: SignUpUserUseCase,
    private val createUserCollectionUseCase: CreateUserCollectionUseCase,
) : ViewModel() {

    fun onRegistrationClick(
        email: String,
        password: String,
        afterSuccess: () -> Unit,
        afterFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            signUpUserUseCase.run(SignUpUserUseCase.Params(email, password))
                .fold(
                    {
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

    private fun createUserCollection(
        userUID: String,
        afterSuccess: () -> Unit,
        afterFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            createUserCollectionUseCase.run(userUID)
                .fold(
                    {
                        val message = resourceProvider.getString(R.string.error_unknown)
                        afterFailure.invoke(message)
                    },
                    { afterSuccess.invoke() }
                )
        }
    }
}