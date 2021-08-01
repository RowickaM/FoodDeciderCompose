package pl.gungnir.fooddecider.ui.screens.login

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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.mics.*
import pl.gungnir.fooddecider.util.helper.isEmailValid
import pl.gungnir.fooddecider.util.helper.isPasswordValid

@ExperimentalComposeUiApi
@Composable
fun Login(
    navController: NavController
) {
    val viewModel by inject<LoginViewModel>(LoginViewModel::class.java)

    val isUserLogged = remember { viewModel.isUserLogged }

    viewModel.onInitialize()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (isUserLogged.value) {
            null -> Loading()
            true -> {
                Loading()
                navController.navigate(NavigationItem.Random.route)
            }
        }
        LoginScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@ExperimentalComposeUiApi
@Composable
private fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val widthPercent = 0.8f
    val focusRequester = remember { FocusRequester() }

    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (dialogMessage, setDialogMessage) = remember { mutableStateOf<LoginDialog>(LoginDialog.Empty) }

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
                title = if (dialogMessage is LoginDialog.OnSuccess) {
                    stringResource(id = R.string.send_email_verification)
                } else {
                    stringResource(id = R.string.cannot_sing_in_user)
                },
                message = dialogMessage.message,
                onChangeVisible = setShowDialog,
                buttonText = if (dialogMessage is LoginDialog.OnEmailNotVerify) {
                    stringResource(id = R.string.send_email_button)
                } else {
                    stringResource(id = R.string.ok)
                },
                onButtonClick = {
                    if (dialogMessage is LoginDialog.OnEmailNotVerify) {
                        viewModel.onSendEmailVerification {
                            setShowDialog(true)
                            setDialogMessage(LoginDialog.OnSuccess(it))
                        }
                    }
                }
            )
        }

        InputOutlined(
            focusRequester = focusRequester,
            widthPercent = widthPercent,
            value = login,
            onValueChange = { setLogin(it) },
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
            type = InputsType.PASSWORD,
            onDone = {
                onLogin(
                    viewModel = viewModel,
                    navController = navController,
                    setDialogVisible = setShowDialog,
                    email = login,
                    password = password,
                    setDialogMessage = setDialogMessage,
                )
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_small)))

        Link(
            modifier = Modifier.fillMaxWidth(widthPercent),
            alignment = TextAlign.End,
            text = stringResource(id = R.string.no_account)
        ) {
            navController.navigate(NavigationItem.Registration.route)
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_small)))

        Link(
            modifier = Modifier.fillMaxWidth(widthPercent),
            alignment = TextAlign.End,
            text = stringResource(id = R.string.reset_password)
        ) {
            navController.navigate(NavigationItem.ForgotPassword.route)
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_xlarge)))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(widthPercent),
            onClick = {
                onLogin(
                    viewModel = viewModel,
                    navController = navController,
                    setDialogVisible = setShowDialog,
                    email = login,
                    password = password,
                    setDialogMessage = setDialogMessage,
                )
            },
            enabled = isFormValid(login, password),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = stringResource(id = R.string.sing_in))
        }
    }
}

private fun isFormValid(email: String, password: String): Boolean {
    return isEmailValid(email) && isPasswordValid(password)
}

private fun onLogin(
    viewModel: LoginViewModel,
    navController: NavController,
    setDialogVisible: (Boolean) -> Unit,
    setDialogMessage: (LoginDialog) -> Unit,
    email: String,
    password: String,
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        viewModel.onLoginClick(
            email = email,
            password = password,
            afterSuccess = { navController.navigate(NavigationItem.Random.route) },
            afterFailure = { isEmailVerifyError, message ->
                setDialogVisible(true)
                if (isEmailVerifyError) {
                    setDialogMessage(LoginDialog.OnEmailNotVerify(message))
                } else {
                    setDialogMessage(LoginDialog.OnFailure(message))
                }
            }
        )
    }
}

private sealed class LoginDialog(val message: String) {
    object Empty : LoginDialog("")
    class OnSuccess(msg: String) : LoginDialog(msg)
    class OnFailure(msg: String) : LoginDialog(msg)
    class OnEmailNotVerify(msg: String) : LoginDialog(msg)
}