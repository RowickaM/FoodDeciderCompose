package pl.gungnir.fooddecider.ui.mics

import androidx.compose.runtime.Composable
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import pl.gungnir.fooddecider.R

@Composable
fun String?.getImage(): ImagePainter {
    return rememberImagePainter(
        data = this ?: "",
        builder = {
            placeholder(R.drawable.ic_logo_foreground)
            error(R.drawable.ic_logo_foreground)
        }
    )
}