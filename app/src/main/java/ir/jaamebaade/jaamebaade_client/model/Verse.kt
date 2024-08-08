package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [ForeignKey(
        entity = Poem::class,
        parentColumns = ["id"],
        childColumns = ["poem_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Verse(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "verse_order") val verseOrder: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "poem_id") val poemId: Int,
)