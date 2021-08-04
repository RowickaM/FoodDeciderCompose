package pl.gungnir.fooddecider.ui.mics

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.credits.iconAuthors
import pl.gungnir.fooddecider.credits.unsplashAuthors
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun Toolbar(
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    title: String? = null,
    onLogout: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val (showInfoDialog, setShowInfoDialog) = remember { mutableStateOf(false) }

    if (showInfoDialog) {
        DialogDisplay(
            title = stringResource(id = R.string.credits),
            onChangeVisible = setShowInfoDialog,
            body = {
                Column {
                    Text(stringResource(id = R.string.photos_in_app))
                    Text(stringResource(id = R.string.authors))
                    LazyColumn {
                        items(items = unsplashAuthors) { credits ->
                            Link(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { openLink(context, credits.link) }
                                    .padding(horizontal = dimensionResource(id = R.dimen.space_small)),
                                text = credits.author
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_small)))

                    Text(stringResource(id = R.string.icons_in_app))
                    Text(stringResource(id = R.string.authors))
                    LazyColumn{
                        items(items = iconAuthors){ author ->
                            Text(text = author)
                        }
                    }
                }
            },
        )
    }

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

            IconButton(
                onClick = {
                    setShowInfoDialog(true)
                }
            ) {
                Icon(
                    modifier = Modifier.wrapContentWidth(Alignment.End),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                )
            }
        }
    }
}

private fun openLink(context:Context, link: String){
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(context, intent, null)
}