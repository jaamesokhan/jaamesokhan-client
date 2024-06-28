package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jaamebaade_client.model.Highlight

@Dao
interface HighlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighlight(highlight: Highlight)
    @Query("SELECT * FROM highlights")

    fun getAll() : List<Highlight>
    @Query("SELECT * FROM highlights WHERE verse_id = :verseId")
    fun getHighlightsForVerse(verseId: Int): List<Highlight>
}