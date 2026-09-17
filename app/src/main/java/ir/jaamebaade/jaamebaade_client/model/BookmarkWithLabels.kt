package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookmarkWithLabels(
    @Embedded val bookmark: Bookmark,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BookmarkLabelCrossRef::class,
            parentColumn = "bookmark_id",
            entityColumn = "label_id"
        )
    )
    val labels: List<Label>,
)
