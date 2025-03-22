package ir.jaamebaade.jaamebaade_client.model

data class BookmarkPoemCategoriesPoetFirstVerse(
    val bookmark: Bookmark,
    val poem: Poem,
    val categories: List<Category>,
    val poet: Poet,
    val firstVerse: Verse
)