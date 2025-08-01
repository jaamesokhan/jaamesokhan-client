package ir.jaamebaade.jaamebaade_client.model

data class HistoryRecordPathFirstVerse
    (val id: Int,
    val timestamp: Long,
    val path: VersePoemCategoriesPoet,
    val firstVerse: Verse
)