package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded

data class BookmarkPoemPoet(
    @Embedded(prefix = "bookmark_")
    val bookmark: Bookmark,
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "poet_")
    val poet: Poet,
)
