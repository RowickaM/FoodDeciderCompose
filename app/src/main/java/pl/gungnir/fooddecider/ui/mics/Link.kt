package pl.gungnir.fooddecider.ui.mics

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Link(
    modifier: Modifier = Modifier,
    alignment: TextAlign? = null,
    text: String,
    onClick: () -> Unit
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