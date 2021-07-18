package pl.gungnir.fooddecider.screens.savedFood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.mics.Toolbar

@Composable
fun SavedFood(
    list: List<String>
) {
    val listFood = remember { list }

    Column {

        Toolbar(title = "LIST OF FOOD")
        LazyColumn {
            items(listFood) { food ->
                SavedFoodItem(name = food)
            }
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