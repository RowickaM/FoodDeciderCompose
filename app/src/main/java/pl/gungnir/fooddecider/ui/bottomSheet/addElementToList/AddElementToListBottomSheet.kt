package pl.gungnir.fooddecider.ui.bottomSheet.addElementToList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import org.koin.java.KoinJavaComponent
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.InputOutlined
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel

@ExperimentalComposeUiApi
@Composable
fun AddElementToListBottomSheet(closeBottomSheet: () -> Unit) {
    val viewModel by KoinJavaComponent.inject<SaveFoodShareViewModel>(SaveFoodShareViewModel::class.java)

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
            value = viewModel.newFood.value,
            onValueChange = viewModel::onFoodNameChange,
            label = stringResource(id = R.string.food_name),
            onDone = {
                viewModel.onAddFoodClick(
                    onSuccess = closeBottomSheet
                )

            }
        )
    }
}