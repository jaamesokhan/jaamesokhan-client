package ir.jaamebaade.jaamebaade_client.model

data class BookmarkPoemCategoriesPoet(
    val bookmark: Bookmark,
    val poem: Poem,
    val categories: List<Category>,
    val poet: Poet,
)