package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Category
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