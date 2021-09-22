package pl.gungnir.fooddecider.ui.screens.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.DialogDisplay
import pl.gungnir.fooddecider.ui.mics.InputOutlined
import pl.gungnir.fooddecider.ui.mics.InputsType
import pl.gungnir.fooddecider.ui.mics.Loading
import pl.gungnir.fooddecider.util.helper.isEmailValid
import pl.gungnir.fooddecider.util.helper.isPasswordValid

@ExperimentalComposeUiApi
@Composable
fun Registration(
    navBack: () -> Unit,
    viewModel: RegistrationViewModel = getViewModel(),
) {
    val widthPercent = 0.8f
    val passwordsNotSameErrorMessage = stringResource(id = R.string.passwords_not_same)
    val focusRequester = remember { FocusRequester() }

    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (repeatPassword, setRepeatPassword) = remember { mutableStateOf("") }

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (dialogMessage, setDialogMessage) = remember {
        mutableStateOf<RegistrationDialog>(
            RegistrationDialog.Empty
        )
    }

    val (showLoading, setShowLoading) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showDialog) {
            DialogDisplay(
                title = if (dialogMessage is RegistrationDialog.OnSuccess) {
                    stringResource(id = R.string.singup_user_success)
                } else {
                    stringResource(id = R.string.cannot_singup_user)
                },
                message = dialogMessage.message,
                onChangeVisible = setShowDialog,
                onButtonClick = {
                    if (dialogMessage is RegistrationDialog.OnSuccess) {
                        navBack()
                    }
                }
            )
        }

        if (showLoading) {
            Loading()
        }

        InputOutlined(
            focusRequester = focusRequester,
            widthPercent = widthPercent,
            value = email,
            onValueChange = { setEmail(it) },
            label = stringResource(id = R.string.email),
            onErrorMessage = stringResource(id = R.string.invalid_format),
            hasNext = true,
            type = InputsType.EMAIL,
            isValidValue = { isEmailValid(it) }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_xlarge)))

        InputOutlined(
            modifier = Modifier.focusRequester(focusRequester),
            widthPercent = widthPercent,
            value = password,
            onValueChange = { setPassword(it) },
            label = stringResource(id = R.string.password),
            onErrorMessage = stringResource(id = R.string.too_short),
            isValidValue = { isPasswordValid(it) },
            hasNext = true,
            type = InputsType.PASSWORD,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_xlarge)))

        InputOutlined(
            modifier = Modifier.focusRequester(focusRequester),
            widthPercent = widthPercent,
            value = repeatPassword,
            onValueChange = { setRepeatPassword(it) },
            label = stringResource(id = R.string.password),
            onErrorMessage = stringResource(id = R.string.too_short),
            isValidValue = { isPasswordValid(it) },
            type = InputsType.PASSWORD,
            onDone = {
                onRegistration(
                    viewModel = viewModel,
                    setDialogVisible = setShowDialog,
                    setLoading = setShowLoading,
                    setDialogMessage = setDialogMessage,
                    email = email,
                    password = password,
                    repeatPassword = repeatPassword,
                    passwordsNotSameInfo = passwordsNotSameErrorMessage
                )
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_small)))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(widthPercent),
            onClick = {
                onRegistration(
                    viewModel = viewModel,
                    setDialogVisible = setShowDialog,
                    setLoading = setShowLoading,
                    setDialogMessage = setDialogMessage,
                    email = email,
                    password = password,
                    repeatPassword = repeatPassword,
                    passwordsNotSameInfo = passwordsNotSameErrorMessage
                )
            },
            enabled = isFormValid(email, password, repeatPassword),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = stringResource(id = R.string.sing_up))
        }
    }
}

private fun isFormValid(email: String, password: String, repeatPassword: String): Boolean {
    return isEmailValid(email) &&
            isPasswordValid(password) &&
            isPasswordValid(repeatPassword)
}

private fun onRegistration(
    viewModel: RegistrationViewModel,
    setDialogVisible: (Boolean) -> Unit,
    setLoading: (Boolean) -> Unit,
    setDialogMessage: (RegistrationDialog) -> Unit,
    email: String,
    password: String,
    repeatPassword: String,
    passwordsNotSameInfo: String,
) {
    if (email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()) {
        if (password != repeatPassword) {
            setDialogVisible(true)
            setDialogMessage(RegistrationDialog.OnFailure(passwordsNotSameInfo))
            return
        } else {
            setLoading(true)
            viewModel.onRegistrationClick(
                email = email,
                password = password,
                afterSuccess = {
                    setLoading(false)
                    setDialogVisible(true)
                    setDialogMessage(RegistrationDialog.OnSuccess(it))
                },
                afterFailure = {
                    setLoading(false)
                    setDialogVisible(true)
                    setDialogMessage(RegistrationDialog.OnFailure(it))
                },
            )
        }
    }
}

private sealed class RegistrationDialog(val message: String) {
    object Empty : RegistrationDialog("")
    class OnSuccess(msg: String) : RegistrationDialog(msg)
    class OnFailure(msg: String) : RegistrationDialog(msg)
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun RegistrationView() {
    Registration(navBack = {})
}