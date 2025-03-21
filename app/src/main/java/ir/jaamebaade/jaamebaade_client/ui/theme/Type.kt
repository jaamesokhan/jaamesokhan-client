package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

val defaultFont = CustomFonts.getDefaultFont()

val Typography = Typography(
    labelSmall = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.label.small.fontWeight,
        fontSize = defaultFont.specs.label.small.fontSize,
        lineHeight = defaultFont.specs.label.small.lineHeight,
        letterSpacing = defaultFont.specs.label.small.letterSpacing
    ),

    labelMedium = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.label.medium.fontWeight,
        fontSize = defaultFont.specs.label.medium.fontSize,
        lineHeight = defaultFont.specs.label.medium.lineHeight,
        letterSpacing = defaultFont.specs.label.medium.letterSpacing
    ),

    labelLarge = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.label.large.fontWeight,
        fontSize = defaultFont.specs.label.large.fontSize,
        lineHeight = defaultFont.specs.label.large.lineHeight,
        letterSpacing = defaultFont.specs.label.large.letterSpacing
    ),

    bodySmall = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.body.small.fontWeight,
        fontSize = defaultFont.specs.body.small.fontSize,
        lineHeight = defaultFont.specs.body.small.lineHeight,
        letterSpacing = defaultFont.specs.body.small.letterSpacing
    ),

    bodyMedium = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.body.medium.fontWeight,
        fontSize = defaultFont.specs.body.medium.fontSize,
        lineHeight = defaultFont.specs.body.medium.lineHeight,
        letterSpacing = defaultFont.specs.body.medium.letterSpacing
    ),

    bodyLarge = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.body.large.fontWeight,
        fontSize = defaultFont.specs.body.large.fontSize,
        lineHeight = defaultFont.specs.body.large.lineHeight,
        letterSpacing = defaultFont.specs.body.large.letterSpacing
    ),

    titleSmall = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.title.small.fontWeight,
        fontSize = defaultFont.specs.title.small.fontSize,
        lineHeight = defaultFont.specs.title.small.lineHeight,
        letterSpacing = defaultFont.specs.title.small.letterSpacing
    ),

    titleMedium = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.title.medium.fontWeight,
        fontSize = defaultFont.specs.title.medium.fontSize,
        lineHeight = defaultFont.specs.title.medium.lineHeight,
        letterSpacing = defaultFont.specs.title.medium.letterSpacing
    ),

    titleLarge = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.title.large.fontWeight,
        fontSize = defaultFont.specs.title.large.fontSize,
        lineHeight = defaultFont.specs.title.large.lineHeight,
        letterSpacing = defaultFont.specs.title.large.letterSpacing
    ),

    displaySmall = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.display.small.fontWeight,
        fontSize = defaultFont.specs.display.small.fontSize,
        lineHeight = defaultFont.specs.display.small.lineHeight,
        letterSpacing = defaultFont.specs.display.small.letterSpacing
    ),

    displayMedium = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.display.medium.fontWeight,
        fontSize = defaultFont.specs.display.medium.fontSize,
        lineHeight = defaultFont.specs.display.medium.lineHeight,
        letterSpacing = defaultFont.specs.display.medium.letterSpacing
    ),

    displayLarge = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.display.large.fontWeight,
        fontSize = defaultFont.specs.display.large.fontSize,
        lineHeight = defaultFont.specs.display.large.lineHeight,
        letterSpacing = defaultFont.specs.display.large.letterSpacing
    ),

    headlineSmall = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.headline.small.fontWeight,
        fontSize = defaultFont.specs.headline.small.fontSize,
        lineHeight = defaultFont.specs.headline.small.lineHeight,
        letterSpacing = defaultFont.specs.headline.small.letterSpacing
    ),

    headlineMedium = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.headline.medium.fontWeight,
        fontSize = defaultFont.specs.headline.medium.fontSize,
        lineHeight = defaultFont.specs.headline.medium.lineHeight,
        letterSpacing = defaultFont.specs.headline.medium.letterSpacing
    ),

    headlineLarge = TextStyle(
        fontFamily = defaultFont.fontFamily,
        fontWeight = defaultFont.specs.headline.large.fontWeight,
        fontSize = defaultFont.specs.headline.large.fontSize,
        lineHeight = defaultFont.specs.headline.large.lineHeight,
        letterSpacing = defaultFont.specs.headline.large.letterSpacing
    )
)