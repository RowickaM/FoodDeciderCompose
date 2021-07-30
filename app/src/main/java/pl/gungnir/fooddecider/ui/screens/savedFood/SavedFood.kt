package pl.gungnir.fooddecider.ui.screens.savedFood

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.*
import pl.gungnir.fooddecider.ui.screens.randomizeFood.Result
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SavedFood() {
    val viewModel by inject<SaveFoodShareViewModel>(SaveFoodShareViewModel::class.java)
    val listFood = viewModel.listOfSavedFood.value

    Column {
        when (listFood) {
            Result.Loading -> Loading()
            is Result.SuccessFetch -> SaveFoodContent(
                viewModel = viewModel,
                listFood = listFood.result
            )
            else -> EmptyInfo(text = stringResource(id = R.string.list_foods_no_foods))
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SaveFoodContent(
    viewModel: SaveFoodShareViewModel,
    listFood: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.space_large)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputOutlined(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.space_large)),
            widthPercent = 1f,
            value = viewModel.newFood.value,
            onValueChange = { viewModel.onFoodNameChange(it) },
            label = stringResource(id = R.string.food_name),
            onDone = { viewModel.onAddFoodClick() }
        )
    }
    LazyColumn {
        itemsIndexed(listFood) { index, food ->
            SavedFoodItem(
                name = food,
                onSwipe = { viewModel.onRemoveFood(index) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SavedFoodItem(
    name: String,
    onSwipe: () -> Unit
) {
    ItemWithSwipe(
        onSwipe = onSwipe
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.space_medium),
                    horizontal = dimensionResource(id = R.dimen.space_large)
                ),
            text = name,
            style = MaterialTheme.typography.body1
        )
    }
}