package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "highlight_label_cross_refs",
    primaryKeys = ["highlight_id", "label_id"],
    foreignKeys = [
        ForeignKey(
            entity = Highlight::class,
            parentColumns = ["id"],
            childColumns = ["highlight_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Label::class,
            parentColumns = ["id"],
            childColumns = ["label_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("highlight_id"), Index("label_id")]
)
data class HighlightLabelCrossRef(
    @ColumnInfo(name = "highlight_id") val highlightId: Int,
    @ColumnInfo(name = "label_id") val labelId: Int,
)
