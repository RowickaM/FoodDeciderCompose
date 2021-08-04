package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                .background(Color(0x80313131))
                .then(modifier),
            verticalArrangement = verticalAlignment,
            content = content
        )
    }
}