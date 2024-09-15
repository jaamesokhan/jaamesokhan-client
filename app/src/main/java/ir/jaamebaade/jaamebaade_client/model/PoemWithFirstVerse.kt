package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded

class PoemWithFirstVerse(
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "verse_")
    val firstVerse: Verse,
)