package pl.gungnir.fooddecider.ui.screens.savedFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.EmptyInfo
import pl.gungnir.fooddecider.ui.mics.ItemWithSwipe
import pl.gungnir.fooddecider.ui.mics.Loading
import pl.gungnir.fooddecider.ui.screens.randomizeFood.Result
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel
import kotlin.random.Random

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
@Preview(showBackground = true)
@Composable
private fun SavedFoodView() {
    SavedFood()
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SaveFoodContent(
    viewModel: SaveFoodShareViewModel,
    listFood: List<String>
) {
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

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun SavedFoodItemView() {
    val (text, onTextChange) = remember { mutableStateOf("swipe to change text") }
    SavedFoodItem(
        name = text,
        onSwipe = {
            onTextChange("swipe, change text ${Random.nextInt(0, 10)}")
        })
}