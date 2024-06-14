package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.jaamebaade_client.model.Poet

@Dao
interface PoetDao {
    @Insert
    fun insertAll(vararg poet: Poet)

    @Query("SELECT * FROM poets")
    fun getAll(): List<Poet>

    @Delete
    fun delete(poet: Poet)

    @Update
    fun update(poet: Poet)
}