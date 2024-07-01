package com.example.jaamebaade_client.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.jaamebaade_client.model.Pair
import com.example.jaamebaade_client.model.Poem

@Dao
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

    @Query("SELECT * FROM poems WHERE category_id = :categoryId")
    fun getPoemsByCategory(categoryId: Int): List<Poem>

    @Query("SELECT * FROM poems WHERE category_id = :categoryId")
    fun getPoemPagingSource(categoryId: Int): PagingSource<Int, Poem>

    @Query("SELECT MIN(id) as first, MAX(id) as second FROM poems WHERE category_id = :categoryId")
    fun getFirstAndLastWithCategoryId(categoryId: Int): Pair


    @Query("SELECT * FROM poems WHERE id = :id")
    fun getPoemById(id: Int): Poem

    @Query("SELECT category_id FROM poems WHERE id = :poemId")

    fun getCategoryByPoemId(poemId: Int): Int
}