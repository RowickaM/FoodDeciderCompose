package pl.gungnir.fooddecider.screens.randomizeFood

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.gungnir.fooddecider.NavigationItem
import pl.gungnir.fooddecider.mics.Toolbar
import kotlin.random.Random

@Composable
fun RandomizeFood(
    navController: NavController
) {
    val randomizeText = remember { mutableStateOf("") }

    Toolbar(
        icon = Icons.Default.Add,
        onIconClick = { navController.navigate(NavigationItem.RandomList.route) }
    )
    Column {
        Spacer(modifier = Modifier.height(150.dp))
        RandomizeFood(
            onClick = {
                randomizeText.value = "Randomize number: ${Random.nextInt(0, 100)}"
            }
        )
        RandomizeResult(result = randomizeText.value)
    }

}

@Composable
fun RandomizeFood(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CLICK TO RANDOMIZE FOOD"
        )
    }
}

@Composable
fun RandomizeResult(space: Dp = 24.dp, result: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(space))
        Text(
            text = result,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(space))
    }
}