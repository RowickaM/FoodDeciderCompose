package pl.gungnir.fooddecider.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent.inject

@ExperimentalComposeUiApi
@Composable
fun Login() {
    val viewModel by inject<LoginViewModel>(LoginViewModel::class.java)
    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = login,
            onValueChange = {
                setLogin(it)
                viewModel.onLoginChange(it)
            },
            label = { Text(text = "email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(0.8f),
            value = password,
            onValueChange = {
                setPassword(it)
                viewModel.onPasswordChange(it)
            },
            label = { Text(text = "password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            onClick = viewModel::onLoginClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Log in")
        }
    }
}