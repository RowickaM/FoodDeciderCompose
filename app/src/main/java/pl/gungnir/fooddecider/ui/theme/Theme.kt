package pl.gungnir.fooddecider.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PurpleLight,
    primaryVariant = PurpleDark,
    secondary = Green
)

private val LightColorPalette = lightColors(
    primary = Purple,
    primaryVariant = PurpleDark,
    secondary = Green,
    background = Base,
    surface = BaseLight,
    onPrimary = BaseLight,
    onSecondary = BaseLight,
    onBackground = DarkGrey,
    onSurface = DarkGrey,
)

@Composable
fun FoodDeciderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}