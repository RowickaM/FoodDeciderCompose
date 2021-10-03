package pl.gungnir.fooddecider.ui.mics

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.credits.iconAuthors
import pl.gungnir.fooddecider.credits.unsplashAuthors

@Composable
fun Toolbar(
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    title: String? = null,
    onLogout: (() -> Unit)? = null
) {

    val (showInfoDialog, setShowInfoDialog) = remember { mutableStateOf(false) }

    if (showInfoDialog) {
        DialogDisplay(
            title = stringResource(id = R.string.credits),
            onChangeVisible = setShowInfoDialog,
            body = { BodyCreditsDialog() },
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
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.wrapContentWidth(Alignment.Start),
                    icon = Icons.Outlined.Logout,
                    contentDescription = stringResource(id = R.string.logout),
                )
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
                IconButton(
                    onClick = onIconClick,
                    modifier = Modifier.wrapContentWidth(Alignment.End),
                    icon = icon,
                    contentDescription = null,
                )
            }

            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                icon = Icons.Outlined.Info,
                contentDescription = null,
                onClick = { setShowInfoDialog(true) }
            )
        }
    }
}

private fun openLink(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(context, intent, null)
}

@Preview(showBackground = true)
@Composable
private fun ToolbarView() {

    Toolbar(
        icon = Icons.Default.Add,
        onIconClick = {},
        title = "title",
        onLogout = {}
    )
}

@Composable
private fun BodyCreditsDialog() {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    val versionName = packageInfo.versionName
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }

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
        LazyColumn {
            items(items = iconAuthors) { author ->
                Text(text = author)
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_default)))

        Text(
            text = stringResource(id = R.string.version_app, versionName, "$versionCode"),
            style = MaterialTheme.typography.subtitle1
        )
    }
}