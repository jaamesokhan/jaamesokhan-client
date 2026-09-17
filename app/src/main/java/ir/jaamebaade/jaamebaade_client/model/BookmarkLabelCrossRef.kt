package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bookmark_label_cross_refs",
    primaryKeys = ["bookmark_id", "label_id"],
    foreignKeys = [
        ForeignKey(
            entity = Bookmark::class,
            parentColumns = ["id"],
            childColumns = ["bookmark_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Label::class,
            parentColumns = ["id"],
            childColumns = ["label_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("bookmark_id"), Index("label_id")]
)
data class BookmarkLabelCrossRef(
    @ColumnInfo(name = "bookmark_id") val bookmarkId: Int,
    @ColumnInfo(name = "label_id") val labelId: Int,
)
