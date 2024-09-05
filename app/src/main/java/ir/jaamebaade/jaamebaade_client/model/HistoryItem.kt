package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = Poet::class,
            parentColumns = ["id"],
            childColumns = ["poet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Poem::class,
            parentColumns = ["id"],
            childColumns = ["poem_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "poet_id") val poetId: Int,
    @ColumnInfo(name = "poem_id") val poemId: Int,
    @ColumnInfo(name = "verse_id") val verseId: Int?,
    val timestamp: Long,
)
