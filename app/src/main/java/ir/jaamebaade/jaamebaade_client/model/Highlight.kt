package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "highlights",
    foreignKeys = [ForeignKey(
        entity = Verse::class,
        parentColumns = ["id"],
        childColumns = ["verse_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["verse_id", "created_at"])]
)
data class Highlight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "verse_id") val verseId: Int,
    @ColumnInfo(name = "start_index") val startIndex: Int,
    @ColumnInfo(name = "end_index") val endIndex: Int,
    @ColumnInfo(name = "created_at", defaultValue = "0") val createdAt: Long = System.currentTimeMillis(),
)
