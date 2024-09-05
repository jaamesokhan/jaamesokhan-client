package ir.jaamebaade.jaamebaade_client.model

data class VisitHistoryViewItem(
    val id: Int,
    val timestamp: Long,
    val versePoemCategoriesPoet: VersePoemCategoriesPoet
)