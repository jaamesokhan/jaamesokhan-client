package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded


data class HighlightVersePoemPoet(
    @Embedded(prefix = "highlight_")
    val highlight: Highlight,
    @Embedded(prefix = "verse_")
    val verse: Verse,
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "poet_")
    val poet: Poet,

    )