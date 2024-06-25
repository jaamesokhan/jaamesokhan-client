package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.model.VerseWithHighlights

@Dao
interface VerseDao {
    @Insert
    fun insertAll(vararg verse: Verse)

    @Query("SELECT * FROM verses")
    fun getAll(): List<Verse>

    @Delete
    fun delete(verse: Verse)

    @Update
    fun update(verse: Verse)

    @Query("SELECT * FROM verses WHERE poem_id = :poemId ORDER BY verse_order")
    fun getPoemVerses(poemId: Int): List<Verse>

    @Query("SELECT * FROM verses WHERE id = :verseId")
    fun getVerseById(verseId: Int): Verse

    @Transaction
    @Query("SELECT * FROM verses WHERE id = :verseId")
    fun getVerseWithHighlights(verseId: Int): VerseWithHighlights
}