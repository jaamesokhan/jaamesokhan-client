package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded
import androidx.room.Relation

data class VerseWithHighlights(
    @Embedded val verse: Verse,
    @Relation(
        parentColumn = "id",
        entityColumn = "verse_id"
    )
    val highlights: List<Highlight>
)