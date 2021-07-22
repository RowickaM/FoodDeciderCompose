package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.ui.NavigationItem
import pl.gungnir.fooddecider.ui.mics.ErrorMessageInput
import pl.gungnir.fooddecider.ui.mics.InputOutlined
import pl.gungnir.fooddecider.ui.mics.InputsType
import pl.gungnir.fooddecider.util.helper.isEmailValid
import pl.gungnir.fooddecider.util.helper.isPasswordValid

@ExperimentalComposeUiApi
@Composable
fun Login(
    navController: NavController
) {
    val widthPercent = 0.8f
    val focusRequester = remember { FocusRequester() }

    val viewModel by inject<LoginViewModel>(LoginViewModel::class.java)

    val (login, setLogin) = remember { mutableStateOf("") }
    val emailError = remember<MutableState<String?>> { mutableStateOf("") }

    val (password, setPassword) = remember { mutableStateOf("") }
    val passwordError = remember<MutableState<String?>> { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputOutlined(
            focusRequester = focusRequester,
            widthPercent = widthPercent,
            value = login,
            onValueChange = {
                emailError.value = if (isEmailValid(login)) {
                    null
                } else {
                    "invalid format"
                }
                setLogin(it)
            },
            label = "email",
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
                    "password is too short "
                }
                setPassword(it)
            },
            label = "password",
            isError = !passwordError.value.isNullOrEmpty(),
            type = InputsType.PASSWORD,
            onDone = {
                viewModel.onLoginClick(login, password) {
                    navController.navigate(NavigationItem.Random.route)
                }
            }
        )

        ErrorMessageInput(text = passwordError.value ?: "", widthPercent = widthPercent)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(widthPercent),
            onClick = {
                viewModel.onLoginClick(login, password) {
                    navController.navigate(NavigationItem.Random.route)
                }
            },
            enabled = emailError.value == null && passwordError.value == null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Log in")
        }
    }
}