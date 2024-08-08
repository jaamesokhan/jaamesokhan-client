package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded

data class VersePoemCategoryPoet(
    @Embedded(prefix = "verse_")
    val verse: Verse,
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "category_")
    val category: Category,
    @Embedded(prefix = "poet_")
    val poet: Poet,
)
