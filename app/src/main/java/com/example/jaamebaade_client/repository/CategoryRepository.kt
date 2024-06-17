package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Category
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class CategoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val categoryDao = db.categoryDao()

    fun getAllCategories() = categoryDao.getAll()

    fun insertCategory(category: Category) = categoryDao.insertAll(category)

    fun deleteCategory(category: Category) = categoryDao.delete(category)

    fun updateCategory(category: Category) = categoryDao.update(category)
}