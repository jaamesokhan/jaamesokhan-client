package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord

@Dao
interface HistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryItem(historyRecord: HistoryRecord)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistorySorted(): List<HistoryRecord>

    @Query("DELETE FROM history WHERE id = :id")
    fun deleteHistoryItem(id: Int)
}