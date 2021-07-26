package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pl.gungnir.fooddecider.R

@Composable
fun Toolbar(
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    title: String? = null,
    onLogout: (() -> Unit)? = null
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
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterStart),
        ) {
            onLogout?.let {
                IconButton(onClick = onLogout) {
                    Icon(
                        modifier = Modifier.wrapContentWidth(Alignment.Start),
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = stringResource(id = R.string.logout),
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Center),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            text = title ?: "",
            style = MaterialTheme.typography.h2
        )

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