package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

fun Modifier.nonRippleClickable(enabled: Boolean = true, onClick: () -> Unit) = composed {
    Modifier
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

@Preview(showBackground = true)
@Composable
private fun NonRippleClickableElement() {
    val (text, changeText) = remember { mutableStateOf("Text. Click to random") }
    Text(
        modifier = Modifier.nonRippleClickable {
            changeText("Link text. Random number ${Random.nextInt(0, 10)}")
        },
        text = text
    )
}