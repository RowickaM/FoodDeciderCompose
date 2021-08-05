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
import androidx.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.DialogError
import pl.gungnir.fooddecider.ui.mics.InputOutlined
import pl.gungnir.fooddecider.ui.mics.InputsType
import pl.gungnir.fooddecider.util.helper.isEmailValid

@ExperimentalComposeUiApi
@Composable
fun ForgotPassword(
    navBack: () -> Unit,
) {
    val viewModel by inject<ForgotPasswordViewModel>(ForgotPasswordViewModel::class.java)

    val widthPercent = 0.8f
    val (email, setEmail) = remember { mutableStateOf("") }

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

        Text(
            modifier = Modifier.fillMaxWidth(widthPercent),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.reset_password_title),
            style = MaterialTheme.typography.h2
        )
        Text(
            modifier = Modifier.fillMaxWidth(widthPercent),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.reset_password_description),
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
                        onSuccess = { navBack() },
                        onFailure = {
                            setShowDialog(true)
                            setDialogMessage(it)
                        }
                    )
                    setEmail("")
                }
            }
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(widthPercent),
            onClick = {
                viewModel.sendLink(
                    email = email,
                    onSuccess = { navBack() },
                    onFailure = {
                        setShowDialog(true)
                        setDialogMessage(it)
                    }
                )
                setEmail("")
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

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun ForgotPasswordView() {
    ForgotPassword(
        navBack = {}
    )
}