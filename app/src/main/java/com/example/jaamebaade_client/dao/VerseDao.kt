package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.jaamebaade_client.model.Verse

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
}