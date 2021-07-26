package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.theme.DarkGrey

@Composable
fun Tag(tagValue: String) {
    Text(
        modifier = Modifier
            .border(
                width = dimensionResource(id = R.dimen.border_width),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.primary
            )
            .background(color = DarkGrey, shape = MaterialTheme.shapes.medium)
            .padding(
                horizontal = dimensionResource(id = R.dimen.space_default),
                vertical = dimensionResource(id = R.dimen.space_xxSmall)
            ),
        text = tagValue,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onPrimary
    )
}