package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Category

class CategoryRepository(context: Context) {
    private val db = DatabaseBuilder.getInstance(context)
    private val categoryDao = db.categoryDao()

    fun getAllCategories() = categoryDao.getAll()

    fun insertCategory(category: Category) = categoryDao.insertAll(category)

    fun deleteCategory(category: Category) = categoryDao.delete(category)

    fun updateCategory(category: Category) = categoryDao.update(category)
}