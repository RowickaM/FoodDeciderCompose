package pl.gungnir.fooddecider.ui.screens.savedFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import org.koin.java.KoinJavaComponent
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.EmptyInfo
import pl.gungnir.fooddecider.ui.mics.Loading
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.screens.randomizeFood.Result
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel

@Composable
fun SavedFood() {
    val viewModel by KoinJavaComponent.inject<SaveFoodShareViewModel>(SaveFoodShareViewModel::class.java)
    val listFood = viewModel.listOfSavedFood.value

    Column {

        Toolbar(title = stringResource(id = R.string.list_foods_title))
        when (listFood) {
            Result.Loading -> Loading()
            is Result.SuccessFetch -> LazyColumn {
                items(listFood.result) { food ->
                    SavedFoodItem(name = food)
                }
            }
            else -> EmptyInfo(text = stringResource(id = R.string.list_foods_no_foods))
        }
    }
}

@Composable
fun SavedFoodItem(
    name: String
) {
    Text(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(id = R.dimen.space_medium),
                horizontal = dimensionResource(id = R.dimen.space_large)
            ),
        text = name,
        style = MaterialTheme.typography.body1
    )
}