package pl.gungnir.fooddecider.ui.mics

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.util.test.TestTags

@ExperimentalAnimationApi
@Composable
fun FoodAnimationOnClick(
    onClick: () -> Unit,
    enabledClick: Boolean,
    durationAnimation: Int = 500,
    height: Dp = 250.dp,
    iconSize: Int = 100
) {
    val visibleFirst = remember { mutableStateOf(true) }
    val visibleSecond = remember { mutableStateOf(false) }
    val visibleThird = remember { mutableStateOf(false) }
    val visibleFourth = remember { mutableStateOf(false) }
    val rotate = remember { mutableStateOf(false) }
    val rotateAnimation by animateFloatAsState(
        targetValue = if (rotate.value) {
            0f
        } else {
            90f
        },
        animationSpec = tween(durationMillis = durationAnimation)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .testTag(TestTags.BOX_DRAW_ANIMATION_TAG)
            .nonRippleClickable(
                onClick = {
                    onClick()
                    rotate.value = !rotate.value
                    when {
                        visibleFirst.value -> {
                            visibleFirst.value = false
                            visibleSecond.value = true
                        }
                        visibleSecond.value -> {
                            visibleSecond.value = false
                            visibleThird.value = true
                        }
                        visibleThird.value -> {
                            visibleThird.value = false
                            visibleFourth.value = true
                        }
                        else -> {
                            visibleFourth.value = false
                            visibleFirst.value = true
                        }
                    }

                },
                enabled = enabledClick
            )
            .wrapContentSize(align = Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .height(height = (height.value / 2 + 60).dp)
                .rotate(rotateAnimation),
            painter = painterResource(id = R.drawable.ic_loupe),
            contentDescription = stringResource(id = R.string.randomize_food_click)
        )

        Box(
            modifier = Modifier
                .height(iconSize.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
        ) {
            FadeInFadeOutAnimation(
                visible = visibleFirst.value,
                durationAnimation = durationAnimation
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apple),
                    contentDescription = null
                )
            }

            FadeInFadeOutAnimation(
                visible = visibleSecond.value,
                durationAnimation = durationAnimation
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pancake),
                    contentDescription = null
                )
            }

            FadeInFadeOutAnimation(
                visible = visibleThird.value,
                durationAnimation = durationAnimation
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_steak),
                    contentDescription = null
                )
            }

            FadeInFadeOutAnimation(
                visible = visibleFourth.value,
                durationAnimation = durationAnimation
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_strawberry),
                    contentDescription = null
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun FoodAnimationOnClickView() {
    FoodAnimationOnClick(onClick = {}, enabledClick = true)
}

@ExperimentalAnimationApi
@Composable
private fun FadeInFadeOutAnimation(
    visible: Boolean,
    durationAnimation: Int,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val delta = 150
    val duration = durationAnimation / 2
    val delay = durationAnimation / 2

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = duration, delayMillis = delay))
                + slideIn(
            animationSpec = tween(durationMillis = duration, delayMillis = delay),
            initialOffset = { IntOffset(delta, 0) }
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = duration))
                + slideOut(
            animationSpec = tween(durationMillis = duration),
            targetOffset = { IntOffset(-delta, 0) }),
        content = content
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun FadeInFadeOutAnimationView() {
    val visible = remember { mutableStateOf(true) }
    Box(modifier = Modifier
        .size(height = 50.dp, width = 50.dp)
        .clickable { visible.value = !visible.value }) {
        FadeInFadeOutAnimation(
            visible = visible.value,
            durationAnimation = 500
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_apple),
                contentDescription = null
            )
        }
    }
}