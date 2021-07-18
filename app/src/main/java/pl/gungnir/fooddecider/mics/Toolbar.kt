package pl.gungnir.fooddecider.mics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    title: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 50.dp)
            .padding(
                top = 24.dp,
                start = 12.dp,
                end = 12.dp
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
                style = MaterialTheme.typography.h3
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