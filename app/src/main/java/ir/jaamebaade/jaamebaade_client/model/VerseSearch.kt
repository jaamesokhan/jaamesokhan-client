package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Verse::class)
@Entity(tableName = "verses_fts")
data class VerseSearch(
    @ColumnInfo(name = "normalized_text")
    val normalizedText: String
)
