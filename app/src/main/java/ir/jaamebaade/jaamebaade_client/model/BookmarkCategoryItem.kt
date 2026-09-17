package ir.jaamebaade.jaamebaade_client.model

data class BookmarkCategoryItem(
    val id: Int,
    val bookmarkSource: BookmarkPoemCategoriesPoetFirstVerse? = null,
    val highlightSource: MergedHighlight? = null,
    val pathText: String,
    val previewText: String,
    val imageUrl: String?,
    val labels: List<Label>,
)
