package ir.jaamebaade.jaamebaade_client.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat


val LocalDarkTheme = compositionLocalOf { false }

/** Stock Material 3 shape scale (the tiers actually public in stable material3 1.4.0). */
private val ExpressiveShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Bottom sheets: top corners only, matching [ExpressiveShapes.extraLarge]'s radius. */
val SheetTopShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

/** Fully-round pill where a chip collapses to a circle. */
val PillShape = RoundedCornerShape(percent = 50)

val ColorScheme.secondaryS30: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).secondaryPalette.tone(30))

val ColorScheme.secondaryS40: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).secondaryPalette.tone(40))

val ColorScheme.secondaryS50: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).secondaryPalette.tone(50))


val ColorScheme.neutralN70: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).neutralPalette.tone(70))

val ColorScheme.neutralN100: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).neutralPalette.tone(100))


val ColorScheme.primary20: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).primaryPalette.tone(20))

val ColorScheme.primary90: Color
    @Composable
    get() = Color(brandScheme(isAppInDarkMode()).primaryPalette.tone(90))

@Composable
fun JaamebaadeclientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: Typography = Typography,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> generateColorScheme(seedColor = BrandSeed, isDark = darkTheme)
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = ExpressiveShapes,
            content = content
        )
    }
}
@Composable
fun isAppInDarkMode(): Boolean {
    return LocalDarkTheme.current
}
