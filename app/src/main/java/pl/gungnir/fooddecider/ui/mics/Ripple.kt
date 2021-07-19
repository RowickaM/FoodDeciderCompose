package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.nonRippleClickable(enabled: Boolean = true, onClick: () -> Unit) = composed {
    Modifier
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

@Composable
fun RippleWrapper(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        content.invoke()
    }
}