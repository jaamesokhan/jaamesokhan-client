package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class HighlightWithLabels(
    @Embedded val highlight: Highlight,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = HighlightLabelCrossRef::class,
            parentColumn = "highlight_id",
            entityColumn = "label_id"
        )
    )
    val labels: List<Label>,
)
