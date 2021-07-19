package pl.gungnir.fooddecider.mics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import pl.gungnir.fooddecider.R

@Composable
fun ImageBackground(
    modifier: Modifier = Modifier,
    image: Painter? = null
) {
    Image(
        painter = image ?: painterResource(id = R.drawable.background_template_default),
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(2f)
            .then(modifier),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ImageBackgroundColumn(
    modifier: Modifier = Modifier,
    modifierImage: Modifier = Modifier,
    image: Painter? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        ImageBackground(
            modifier = Modifier.align(Alignment.Center),
            image = image
        )
        Image(
            painter = image ?: painterResource(id = R.drawable.background_template_default),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(2f)
                .then(modifierImage),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0x80313131))
                .then(modifier),
            verticalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}