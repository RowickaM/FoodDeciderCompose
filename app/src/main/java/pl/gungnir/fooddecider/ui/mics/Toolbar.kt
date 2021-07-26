package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pl.gungnir.fooddecider.R

@Composable
fun Toolbar(
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    title: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.height_toolbar))
            .padding(
                top = dimensionResource(id = R.dimen.space_xlarge),
                start = dimensionResource(id = R.dimen.space_xMedium),
                end = dimensionResource(id = R.dimen.space_xMedium),
            ),
    ) {
        title?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                text = title,
                style = MaterialTheme.typography.h6
            )
        }

        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd),
        ) {
            icon?.let {
                IconButton(onClick = onIconClick) {
                    Icon(
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}