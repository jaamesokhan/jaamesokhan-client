package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Category
import javax.inject.Inject


class CategoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val categoryDao = db.categoryDao()

    fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)

    fun getCategoriesByPoetIdFilteredByParentId(poetId: Int, parentId: Int) =
        categoryDao.getCategoriesByPoetIdFilteredByParentId(poetId, parentId)

    fun getPoetCategoryId(poetId: Int) =
        categoryDao.getPoetCategoryId(poetId)

    fun insertCategory(category: Category) = categoryDao.insertAll(category)
}