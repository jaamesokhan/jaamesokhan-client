package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HistoryItem

@Dao
interface HistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryItem(historyItem: HistoryItem)

    @Query("SELECT * FROM history")
    fun getAllHistory(): List<HistoryItem>

    @Query("DELETE FROM history WHERE id = :id")
    fun deleteHistoryItem(id: Int)
}