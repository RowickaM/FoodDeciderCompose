package pl.gungnir.fooddecider.ui.mics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@ExperimentalMaterialApi
@Composable
fun ItemWithSwipe(
    modifier: Modifier = Modifier,
    onSwipe: () -> Unit,
    icon: ImageVector = Icons.Default.Delete,
    backgroundOnSwipe: Color = Color.Red,
    backgroundOnIdle: Color = MaterialTheme.colors.background,
    body: @Composable RowScope.() -> Unit
) {

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onSwipe()
            }
            it != DismissValue.DismissedToStart
        }
    )

    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .then(modifier),
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.1f) },
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss

            val state by animateDpAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 10.dp else (-10).dp,
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            )

            val color by animateColorAsState(
                if (dismissState.targetValue == DismissValue.Default) {
                    backgroundOnIdle
                } else {
                    backgroundOnSwipe
                }
            )
            val alpha by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0f else 1f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .absoluteOffset(state)
                    .graphicsLayer(alpha = alpha)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                )
            }
        },

        dismissContent = body
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ItemWithSwipeView() {
    val (text, changeText) = remember { mutableStateOf("swipe to action") }
    ItemWithSwipe(
        modifier = Modifier.fillMaxWidth(),
        onSwipe = {
            changeText("change text, random number: ${Random.nextInt(0, 10)}")
        }
    ) {
        Text(text = text)
    }
}