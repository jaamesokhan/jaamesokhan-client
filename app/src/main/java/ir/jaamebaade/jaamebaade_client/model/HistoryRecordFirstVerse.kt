package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded

data class HistoryRecordFirstVerse(
    @Embedded(prefix = "history_")
    val history: HistoryRecord,
    @Embedded(prefix = "verse_")
    val firstVerse: Verse
)