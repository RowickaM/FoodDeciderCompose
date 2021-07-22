package pl.gungnir.fooddecider.ui.screens.savedFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import pl.gungnir.fooddecider.ui.mics.EmptyInfo
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.screens.randomizeFood.Result
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel

@Composable
fun SavedFood() {
    val viewModel by KoinJavaComponent.inject<SaveFoodShareViewModel>(SaveFoodShareViewModel::class.java)
    val listFood = viewModel.listOfSavedFood.value

    Column {

        Toolbar(title = "LIST OF FOOD")
        when (listFood) {
            Result.Loading -> Text(text = "Loading...")
            is Result.SuccessFetch -> LazyColumn {
                items(listFood.result) { food ->
                    SavedFoodItem(name = food)
                }
            }
            else -> EmptyInfo(text = "You don't have saved food")
        }
    }
}

@Composable
fun SavedFoodItem(
    name: String
) {
    Text(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
        text = name,
        style = MaterialTheme.typography.body1
    )
}