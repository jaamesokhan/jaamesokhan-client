package ir.jaamebaade.jaamebaade_client.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_history",
    indices = [Index(value = ["query"], unique = true)])
data class SearchHistoryRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val timestamp: Long
)
