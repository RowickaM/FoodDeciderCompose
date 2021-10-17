package pl.gungnir.fooddecider.ui.bottomSheet.addList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.InputOutlined

@ExperimentalComposeUiApi
@Composable
fun AddListBottomSheet(
    closeBottomSheet: () -> Unit,
    onDone: (String) -> Unit
) {
    val (value, onChangeValue) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.space_large),
                vertical = dimensionResource(id = R.dimen.space_default)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputOutlined(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.space_large)),
            widthPercent = 1f,
            value = value,
            onValueChange = onChangeValue,
            label = stringResource(id = R.string.list_name),
            onDone = {
                onDone(value)
                closeBottomSheet()
            }
        )
    }
}