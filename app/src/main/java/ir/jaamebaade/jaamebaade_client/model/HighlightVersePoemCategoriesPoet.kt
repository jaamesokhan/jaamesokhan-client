package ir.jaamebaade.jaamebaade_client.model


data class HighlightVersePoemCategoriesPoet(
    val highlight: Highlight,
    val versePath: VersePoemCategoriesPoet,
)

fun HighlightVersePoemCategoriesPoet.toMergedHighlight(): MergedHighlight =
    MergedHighlight(
        highlights = mutableListOf(highlight),
        verses = mutableListOf(versePath.verse!!),
        poem = versePath.poem,
        categories = versePath.categories,
        poet = versePath.poet
    )

