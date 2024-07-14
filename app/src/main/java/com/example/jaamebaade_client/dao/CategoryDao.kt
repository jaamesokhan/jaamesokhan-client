package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.jaamebaade_client.model.Category

@Dao
interface CategoryDao {
    @Insert
    fun insertAll(vararg category: Category)

    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Delete
    fun delete(category: Category)

    @Update
    fun update(category: Category)

    @Query(
        """
            SELECT id from categories
                WHERE poet_id = :poetId
                    AND parent_id = 0
        """
    )
    fun getPoetCategoryId(poetId: Int): Int

    @Query(
        """SELECT * FROM categories 
            WHERE poet_id = :poetId 
                AND parent_id = :parentId
        """
    )
    fun getCategoriesByPoetIdFilteredByParentId(poetId: Int, parentId: Int): List<Category>

    @Query(
        """
            SELECT * FROM categories
                WHERE id = :id
        """
    )
    fun getCategoryById(id: Int): Category
}