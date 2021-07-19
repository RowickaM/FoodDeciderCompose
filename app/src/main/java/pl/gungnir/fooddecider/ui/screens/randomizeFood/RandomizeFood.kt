package pl.gungnir.fooddecider.ui.screens.randomizeFood

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pl.gungnir.fooddecider.NavigationItem
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.mics.nonRippleClickable

@Composable
fun RandomizeFood(
    navController: NavController,
    viewModel: RandomizeFoodViewModel = viewModel()
) {
    viewModel.onInitialize()
    val foodResult = viewModel.randomFood.value
    Toolbar(
        icon = Icons.Default.Add,
        onIconClick = { navController.navigate(NavigationItem.RandomList.route) }
    )
    Column {
        Spacer(modifier = Modifier.height(150.dp))
        RandomizeFood(
            onClick = viewModel::drawFood,
            enabledClick = foodResult != Result.Loading
        )
        RandomizeResult(result = foodResult)
    }

}

@Composable
fun RandomizeFood(
    onClick: () -> Unit,
    enabledClick: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .nonRippleClickable(onClick = onClick, enabled = enabledClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CLICK TO RANDOMIZE FOOD"
        )
    }
}

@Composable
fun RandomizeResult(space: Dp = 24.dp, result: Result) {
    val text = when (result) {
        Result.Loading -> {
            "Loading..."
        }
        is Result.Success -> {
            result.result
        }
        else -> {
            "Click image to draw food"
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(space))

        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(space))
    }
}