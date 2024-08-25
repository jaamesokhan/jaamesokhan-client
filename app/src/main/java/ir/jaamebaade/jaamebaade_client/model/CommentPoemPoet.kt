package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded

data class CommentPoemPoet(
    @Embedded(prefix = "comment_")
    val comment: Comment,
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "poet_")
    val poet: Poet,
)