package pl.gungnir.fooddecider.ui.mics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun Link(
    modifier: Modifier = Modifier,
    alignment: TextAlign? = null,
    text: String,
    onClick: () -> Unit = {}
) {
    Text(
        modifier = Modifier
            .nonRippleClickable(onClick = onClick)
            .then(modifier),
        textAlign = alignment,
        text = text,
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary
    )
}

@Preview(showBackground = true)
@Composable
private fun LinkView() {
    val (text, changeText) = remember { mutableStateOf("Link text. Click to random") }

    Link(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        onClick = { changeText("Link text. Random number ${Random.nextInt(0, 10)}") }
    )
}