package pl.gungnir.fooddecider.ui.screens.forgotPassword

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.InputOutlined
import pl.gungnir.fooddecider.ui.mics.InputsType
import pl.gungnir.fooddecider.util.helper.isEmailValid

@ExperimentalComposeUiApi
@Composable
fun ForgotPassword(
    navController: NavController
) {
    val viewModel by inject<ForgotPasswordViewModel>(ForgotPasswordViewModel::class.java)

    val widthPercent = 0.8f
    val (email, setEmail) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(widthPercent),
            textAlign = TextAlign.Center,
            text = "Podaj email powiązany z twoim kontem.",
            style = MaterialTheme.typography.h2
        )
        Text(
            modifier = Modifier.fillMaxWidth(widthPercent),
            textAlign = TextAlign.Center,
            text = "Wyślwmy Ci mail, umożliwiający resetowanie hasła.",
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_xxLarge)))

        InputOutlined(
            widthPercent = widthPercent,
            value = email,
            onValueChange = setEmail,
            label = stringResource(id = R.string.email),
            type = InputsType.EMAIL,
            onErrorMessage = stringResource(id = R.string.invalid_format),
            isValidValue = { isEmailValid(it) },
            onDone = {
                if (isEmailValid(email)) {
                    viewModel.sendLink(
                        email = email,
                        onSuccess = { onEmailSend(navController = navController) },
                        onFailure = {}
                    )
                }
            }
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(widthPercent),
            onClick = {
                viewModel.sendLink(
                    email = email,
                    onSuccess = { onEmailSend(navController = navController) },
                    onFailure = {}
                )
            },
            enabled = isEmailValid(email),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = stringResource(id = R.string.send_link))
        }
    }
}

private fun onEmailSend(navController: NavController) {
    navController.navigateUp()
}

private fun onError(errorMessage: String) {

}