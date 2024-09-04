package ir.jaamebaade.jaamebaade_client.model

data class MergedHighlight (
    val highlights: MutableList<Highlight>,
    val verses: MutableList<Verse>,
    val poem: Poem,
    val categories: List<Category>,
    val poet: Poet
)