package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.materialkolor.dynamiccolor.DynamicColor
import com.materialkolor.dynamiccolor.MaterialDynamicColors
import com.materialkolor.hct.Hct
import com.materialkolor.scheme.DynamicScheme
import com.materialkolor.scheme.SchemeTonalSpot

/** The app's brand color. Seeds the whole generated Material 3 tonal palette. */
val BrandSeed = Color(0xFF5B6642)

/**
 * Fixed brand marker-green pair, used as a highlight-text background (`VerseItem`), the
 * selected-chip color in bookmark/highlight chips, and the `RandomPoemBox` gradient. Not a
 * semantic M3 tertiary role — these call sites need a color that stays a legible mid-tone
 * green under plain body text in both themes, not one that swaps to tone 80/near-white in
 * dark mode the way a generated `tertiary` role correctly would for its own intended use.
 */
private val tertiaryMarkerLight = Color(0xFF718053)
private val onTertiaryMarkerLight = Color(0xFFFFFFFF)
private val tertiaryContainerMarkerLight = Color(0xFF323825)
private val onTertiaryContainerMarkerLight = Color(0xFFFFFFFF)
private val tertiaryMarkerDark = Color(0xFF5B6642)
private val onTertiaryMarkerDark = Color(0xFFFFFFFF)
private val tertiaryContainerMarkerDark = Color(0xFF323825)
private val onTertiaryContainerMarkerDark = Color(0xFFFFFFFF)

/**
 * Fixed muted-gray pair. Real M3 `outline`/`outlineVariant` are low-contrast border tones
 * (dark-mode `outlineVariant` is tone 30, nearly as dark as the background itself) — but this
 * app uses these two roles as its secondary/tertiary body-text and icon color throughout
 * (~40 call sites: list subtitles, empty-state text, dividers, hint text). Keeping the
 * original legible gray values here instead of the spec's border tones.
 */
private val outlineLight = Color(0xFFCCCCCC)
private val outlineVariantLight = Color(0xFF999999)
private val outlineDark = Color(0xFF3A3A3A)
private val outlineVariantDark = Color(0xFFADADAD)

/** Builds the TonalSpot dynamic scheme for [BrandSeed] at the given brightness. */
internal fun brandScheme(isDark: Boolean): DynamicScheme = SchemeTonalSpot(
    sourceColorHct = Hct.fromInt(BrandSeed.toArgb()),
    isDark = isDark,
    contrastLevel = 0.0,
)

private fun DynamicColor.toColor(scheme: DynamicScheme): Color = Color(getArgb(scheme))

/**
 * Generates a stock Material 3 tonal [ColorScheme] from [seedColor] using the TonalSpot
 * algorithm (the same one Material Theme Builder uses by default), so the seed hue stays
 * dominant as `primary` rather than being shifted away from, as MaterialKolor's own
 * `PaletteStyle.Expressive` would do.
 */
fun generateColorScheme(seedColor: Color, isDark: Boolean): ColorScheme {
    val scheme = SchemeTonalSpot(
        sourceColorHct = Hct.fromInt(seedColor.toArgb()),
        isDark = isDark,
        contrastLevel = 0.0,
    )
    val colors = MaterialDynamicColors()

    return ColorScheme(
        background = colors.background().toColor(scheme),
        error = colors.error().toColor(scheme),
        errorContainer = colors.errorContainer().toColor(scheme),
        inverseOnSurface = colors.inverseOnSurface().toColor(scheme),
        inversePrimary = colors.inversePrimary().toColor(scheme),
        inverseSurface = colors.inverseSurface().toColor(scheme),
        onBackground = colors.onBackground().toColor(scheme),
        onError = colors.onError().toColor(scheme),
        onErrorContainer = colors.onErrorContainer().toColor(scheme),
        onPrimary = colors.onPrimary().toColor(scheme),
        onPrimaryContainer = colors.onPrimaryContainer().toColor(scheme),
        onSecondary = colors.onSecondary().toColor(scheme),
        onSecondaryContainer = colors.onSecondaryContainer().toColor(scheme),
        onSurface = colors.onSurface().toColor(scheme),
        onSurfaceVariant = colors.onSurfaceVariant().toColor(scheme),
        onTertiary = if (isDark) onTertiaryMarkerDark else onTertiaryMarkerLight,
        onTertiaryContainer = if (isDark) onTertiaryContainerMarkerDark else onTertiaryContainerMarkerLight,
        outline = if (isDark) outlineDark else outlineLight,
        outlineVariant = if (isDark) outlineVariantDark else outlineVariantLight,
        primary = colors.primary().toColor(scheme),
        primaryContainer = colors.primaryContainer().toColor(scheme),
        scrim = colors.scrim().toColor(scheme),
        secondary = colors.secondary().toColor(scheme),
        secondaryContainer = colors.secondaryContainer().toColor(scheme),
        surface = colors.surface().toColor(scheme),
        surfaceTint = colors.surfaceTint().toColor(scheme),
        surfaceBright = colors.surfaceBright().toColor(scheme),
        surfaceDim = colors.surfaceDim().toColor(scheme),
        surfaceContainer = colors.surfaceContainer().toColor(scheme),
        surfaceContainerHigh = colors.surfaceContainerHigh().toColor(scheme),
        surfaceContainerHighest = colors.surfaceContainerHighest().toColor(scheme),
        surfaceContainerLow = colors.surfaceContainerLow().toColor(scheme),
        surfaceContainerLowest = colors.surfaceContainerLowest().toColor(scheme),
        surfaceVariant = colors.surfaceVariant().toColor(scheme),
        tertiary = if (isDark) tertiaryMarkerDark else tertiaryMarkerLight,
        tertiaryContainer = if (isDark) tertiaryContainerMarkerDark else tertiaryContainerMarkerLight,
        primaryFixed = colors.primaryFixed().toColor(scheme),
        primaryFixedDim = colors.primaryFixedDim().toColor(scheme),
        onPrimaryFixed = colors.onPrimaryFixed().toColor(scheme),
        onPrimaryFixedVariant = colors.onPrimaryFixedVariant().toColor(scheme),
        secondaryFixed = colors.secondaryFixed().toColor(scheme),
        secondaryFixedDim = colors.secondaryFixedDim().toColor(scheme),
        onSecondaryFixed = colors.onSecondaryFixed().toColor(scheme),
        onSecondaryFixedVariant = colors.onSecondaryFixedVariant().toColor(scheme),
        tertiaryFixed = colors.tertiaryFixed().toColor(scheme),
        tertiaryFixedDim = colors.tertiaryFixedDim().toColor(scheme),
        onTertiaryFixed = colors.onTertiaryFixed().toColor(scheme),
        onTertiaryFixedVariant = colors.onTertiaryFixedVariant().toColor(scheme),
    )
}

/**
 * Fixed (theme-independent) near-white neutral tone, for surfaces that always stay light
 * regardless of app theme (e.g. a card rendered on a fixed light background).
 */
val neutralN95Light: Color = Color(brandScheme(isDark = false).neutralPalette.tone(95))
