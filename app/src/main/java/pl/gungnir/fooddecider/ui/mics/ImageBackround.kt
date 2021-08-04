package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import pl.gungnir.fooddecider.R

@ExperimentalCoilApi
@Composable
fun ImageBackground(
    modifier: Modifier = Modifier,
    image: ImagePainter? = null
) {
    Image(
        painter = image ?: painterResource(id = R.drawable.background_template_default),
        contentDescription = null,
        modifier = Modifier.then(modifier),
        contentScale = ContentScale.Crop,
        colorFilter = if (image?.state is ImagePainter.State.Success) {
            ColorFilter.lighting(Color(0x80313131), Color(0x80313131))
        } else {
            null
        }
    )
}

@ExperimentalCoilApi
@Composable
fun ImageBackgroundColumn(
    modifier: Modifier = Modifier,
    modifierImage: Modifier = Modifier,
    image: ImagePainter? = null,
    verticalAlignment: Arrangement.HorizontalOrVertical = Arrangement.SpaceBetween,
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        ImageBackground(
            modifier = Modifier
                .align(Alignment.Center)
                .then(modifierImage),
            image = image
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            verticalArrangement = verticalAlignment,
            content = content
        )
    }
}