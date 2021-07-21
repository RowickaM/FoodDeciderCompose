package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.ui.theme.DarkGrey

@Composable
fun Tag(tagValue: String) {
    Text(
        modifier = Modifier
            .border(
                width = 2.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.primary
            )
            .background(color = DarkGrey, shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        text = tagValue,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onPrimary
    )
}