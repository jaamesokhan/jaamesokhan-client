package com.example.jaamebaade_client.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.jaamebaade_client.model.Poem

interface PoemDao {

    @Transaction
    @Insert
    fun insertAll(vararg poem: Poem)

    @Transaction
    @Query("SELECT * FROM poems")
    fun getAll(): List<Poem>

    @Transaction
    @Delete
    fun delete(poem: Poem)

    @Transaction
    @Update
    fun update(poem: Poem)
}