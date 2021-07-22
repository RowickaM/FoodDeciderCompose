package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

enum class InputsType {
    EMAIL,
    PASSWORD,
    TEXT
}

@ExperimentalComposeUiApi
@Composable
fun InputOutlined(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    widthPercent: Float,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    hasNext: Boolean = false,
    type: InputsType = InputsType.TEXT,
    onDone: () -> Unit = {}
) {
    val keyboardType = when (type) {
        InputsType.TEXT -> KeyboardType.Text
        InputsType.EMAIL -> KeyboardType.Email
        InputsType.PASSWORD -> KeyboardType.Password
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val (showPassword, setShowPassword) = remember { mutableStateOf(false) }
    val transformation = if (type == InputsType.PASSWORD) {
        if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(widthPercent)
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (hasNext) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone()
            },
            onNext = {
                focusRequester?.requestFocus()
            }),

        visualTransformation = transformation,
        trailingIcon = {
            if (type == InputsType.PASSWORD) {
                Icon(
                    imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = "show password",
                    modifier = Modifier.nonRippleClickable {
                        setShowPassword(!showPassword)
                    }
                )
            }
        },
        isError = isError,
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun ErrorMessageInput(text: String, widthPercent: Float) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 12.sp,
        modifier = Modifier.fillMaxWidth(widthPercent),
        textAlign = TextAlign.End
    )
}