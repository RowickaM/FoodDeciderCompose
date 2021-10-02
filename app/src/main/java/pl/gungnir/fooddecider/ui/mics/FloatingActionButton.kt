package pl.gungnir.fooddecider.ui.mics

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.Composable

@Composable
fun FloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Topic, contentDescription = "click to show lists")
    }
}