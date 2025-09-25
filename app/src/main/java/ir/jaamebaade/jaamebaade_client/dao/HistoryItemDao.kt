package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordFirstVerse
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord

@Dao
interface HistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryItem(historyRecord: HistoryRecord)

    @Query(
        "SELECT h.id as history_id, h.poem_id as history_poem_id, h.timestamp as history_timestamp," +
                "v.id AS verse_id, v.text as verse_text," +
                "v.verse_order as verse_verse_order, v.position as verse_position, v.poem_id as verse_poem_id" +
                " FROM history h JOIN verses v ON v.poem_id = h.poem_id\n" +
                "            WHERE v.verse_order = 1 ORDER BY timestamp DESC"
    )
    fun getAllHistorySortedWithFirstVerse(): List<HistoryRecordFirstVerse>

    @Query("DELETE FROM history WHERE id = :id")
    fun deleteHistoryItem(id: Int)

    @Query("DELETE FROM history")
    fun clearHistory()
}
