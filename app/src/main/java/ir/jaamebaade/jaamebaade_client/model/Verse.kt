package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [ForeignKey(
        entity = Poem::class,
        parentColumns = ["id"],
        childColumns = ["poem_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("poem_id")]
)
data class Verse(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "verse_order") val verseOrder: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "poem_id") val poemId: Int,
    // TODO Add a normalized text column for search correctness
)