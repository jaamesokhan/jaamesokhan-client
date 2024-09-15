package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistoryRecord(searchHistoryRecord: SearchHistoryRecord)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getSearchHistoryRecords(): Flow<List<SearchHistoryRecord>>

    @Query("DELETE FROM search_history WHERE id = :id")
    fun removeSearchHistoryRecord(id: Int)
}