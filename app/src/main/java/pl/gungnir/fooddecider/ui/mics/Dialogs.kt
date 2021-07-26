package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.R

@Composable
fun DialogError(
    title: String,
    message: String,
    onChangeVisible: (Boolean) -> Unit
) {
    DialogDisplay(
        title = title,
        onChangeVisible = onChangeVisible,
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onChangeVisible(false) }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }

        }) {
        Text(text = message)
    }
}

@Composable
fun DialogDisplay(
    title: String,
    onChangeVisible: (Boolean) -> Unit,
    buttons: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onChangeVisible(false) },
        buttons = buttons,
        title = { Text(text = title, style = MaterialTheme.typography.h6) },
        text = body
    )
}