package pl.gungnir.fooddecider.ui.mics

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pl.gungnir.fooddecider.util.test.TestTags

@Composable
fun FloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.testTag(TestTags.FAB_TAG),
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Topic, contentDescription = "click to show lists")
    }
}