package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.NavigationItem
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
    val emailErrorText = stringResource(id = R.string.invalid_format)
    val passwordErrorText = stringResource(id = R.string.too_short)

    val (login, setLogin) = remember { mutableStateOf("") }
    val emailError = remember<MutableState<String?>> { mutableStateOf("") }

    val (password, setPassword) = remember { mutableStateOf("") }
    val passwordError = remember<MutableState<String?>> { mutableStateOf("") }

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (dialogMessage, setDialogMessage) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showDialog) {
            DialogError(
                title = stringResource(id = R.string.cannot_sing_in),
                message = dialogMessage,
                onChangeVisible = setShowDialog
            )
        }

        InputOutlined(
            focusRequester = focusRequester,
            widthPercent = widthPercent,
            value = login,
            onValueChange = {
                emailError.value = if (isEmailValid(login)) {
                    null
                } else {
                    emailErrorText
                }
                setLogin(it)
            },
            label = stringResource(id = R.string.email),
            isError = !emailError.value.isNullOrEmpty(),
            hasNext = true,
            type = InputsType.EMAIL,
        )

        ErrorMessageInput(text = emailError.value ?: "", widthPercent = widthPercent)

        Spacer(modifier = Modifier.height(20.dp))

        InputOutlined(
            modifier = Modifier.focusRequester(focusRequester),
            widthPercent = widthPercent,
            value = password,
            onValueChange = {
                passwordError.value = if (isPasswordValid(password)) {
                    null
                } else {
                    passwordErrorText
                }
                setPassword(it)
            },
            label = stringResource(id = R.string.password),
            isError = !passwordError.value.isNullOrEmpty(),
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

        ErrorMessageInput(text = passwordError.value ?: "", widthPercent = widthPercent)

        Spacer(modifier = Modifier.height(20.dp))

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
            enabled = emailError.value == null && passwordError.value == null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = stringResource(id = R.string.sing_in))
        }
    }
}

private fun onLogin(
    viewModel: LoginViewModel,
    navController: NavController,
    setDialogVisible: (Boolean) -> Unit,
    setDialogMessage: (String) -> Unit,
    email: String,
    password: String,
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        viewModel.onLoginClick(
            email = email,
            password = password,
            afterSuccess = { navController.navigate(NavigationItem.Random.route) },
            afterFailure = {
                setDialogVisible(true)
                setDialogMessage(it)
            }
        )
    }
}