package ir.jaamebaade.jaamebaade_client.model

data class VersePoemCategoriesPoet (
    val verse: Verse,
    val poem: Poem,
    val categories: List<Category>,
    val poet: Poet
)