package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import pl.gungnir.fooddecider.R

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
    widthPercent: Float = 1f,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
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
                focusRequester?.freeFocus()
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
                    contentDescription = stringResource(id = R.string.content_description_toggle_password),
                    modifier = Modifier.nonRippleClickable {
                        setShowPassword(!showPassword)
                    }
                )
            }
        },
        singleLine = true,
        maxLines = 1,
    )
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun InputOutlinedView() {
    val (value, onValueChange) = remember { mutableStateOf("") }
    InputOutlined(
        value = value,
        onValueChange = onValueChange,
        label = "label",
        type = InputsType.PASSWORD
    )
}

@ExperimentalComposeUiApi
@Composable
fun InputOutlined(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    widthPercent: Float = 1f,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onErrorMessage: String,
    isValidValue: (String) -> Boolean,
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

    val hasError = remember { mutableStateOf(false) }
    val hasFocus = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(widthPercent)
            .onFocusChanged {
                if (!it.isFocused && hasFocus.value) {
                    hasFocus.value = false
                    hasError.value = !isValidValue(value)
                }

                if (it.isFocused) {
                    hasFocus.value = true
                }
            }
            .then(modifier),
        value = value,
        onValueChange = {
            if (hasError.value) {
                hasError.value = !isValidValue(it)
            }
            onValueChange(it)
        },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (hasNext) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusRequester?.freeFocus()
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
                    contentDescription = stringResource(id = R.string.content_description_toggle_password),
                    modifier = Modifier.nonRippleClickable {
                        setShowPassword(!showPassword)
                    }
                )
            }
        },
        isError = hasError.value,
        singleLine = true,
        maxLines = 1,
    )

    ErrorMessageInput(
        text = if (hasError.value) onErrorMessage else "",
        widthPercent = widthPercent
    )
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun InputOutlinedWithErrorView() {
    val (value, onValueChange) = remember { mutableStateOf("") }
    val hasError = value.isNotEmpty()
    InputOutlined(
        value = value,
        onValueChange = onValueChange,
        label = "label",
        onErrorMessage = "error",
        isValidValue = { hasError },
        type = InputsType.PASSWORD
    )
}

@Composable
fun ErrorMessageInput(text: String, widthPercent: Float) {
    Text(
        text = text,
        color = Color.Red,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.fillMaxWidth(widthPercent),
        textAlign = TextAlign.End
    )
}


@Preview(showBackground = true)
@Composable
private fun ErrorMessageInputView() {
    ErrorMessageInput(
        text = "error message",
        widthPercent = 1f
    )
}