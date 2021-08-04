package pl.gungnir.fooddecider.ui.screens.randomizeFood

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.FoodAnimationOnClick
import pl.gungnir.fooddecider.util.RANDOM_FOOD_TIME

@ExperimentalAnimationApi
@Composable
fun RandomizeFood() {
    val viewModel by inject<SaveFoodShareViewModel>(SaveFoodShareViewModel::class.java)

    viewModel.onInitialize()
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