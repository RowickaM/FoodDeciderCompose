package pl.gungnir.fooddecider.ui.screens.randomizeFood

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.FoodAnimationOnClick
import pl.gungnir.fooddecider.ui.mics.nonRippleClickable
import pl.gungnir.fooddecider.util.RANDOM_FOOD_TIME

@ExperimentalAnimationApi
@Composable
fun RandomizeFood(
    setSavedList: (List<String>, String) -> Unit = { _, _ -> },
    viewModel: SaveFoodShareViewModel = getViewModel(),
) {
    viewModel.onInitialize(setSavedList)
    val foodResult = viewModel.randomFood.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        FoodAnimationOnClick(
            onClick = viewModel::drawFood,
            enabledClick = foodResult != Result.Loading,
            durationAnimation = RANDOM_FOOD_TIME
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_large)))
        RandomizeResult(result = foodResult)
    }

}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun RandomizeFoodView() {
    RandomizeFood()
}

@Composable
fun RandomizeResult(result: Result) {
    val text = when (result) {
        Result.Loading -> {
            stringResource(id = R.string.randomize_food_loading)
        }
        is Result.Success -> {
            result.result
        }
        else -> {
            stringResource(id = R.string.randomize_food_image_click)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.space_xxLarge))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomizeResultView() {
    val (result, setResult) = remember {
        mutableStateOf<Result>(Result.Empty)
    }
    Box(modifier = Modifier.nonRippleClickable {
        val newResult = when (result) {
            Result.Empty -> Result.Loading
            Result.Loading -> Result.Success("result")
            else -> Result.Loading
        }
        setResult(newResult)
    }) {
        RandomizeResult(result = result)
    }
}