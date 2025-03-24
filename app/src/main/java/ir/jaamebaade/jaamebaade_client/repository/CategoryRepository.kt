package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Category
import javax.inject.Inject


class CategoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val categoryDao = db.categoryDao()

    fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)

    fun getPoetCategoryId(poetId: Int) =
        categoryDao.getPoetCategoryId(poetId)

    fun getAllParentsOfCategoryId(poemId: Int) = categoryDao.getAllParentsOfCategoryId(poemId)

    fun getCategoriesByPoetIdFilteredByParentIdWithPoemCount(poetId: Int, parentId: Int) =
        categoryDao.getCategoriesByPoetIdFilteredByParentIdWithPoemCount(poetId, parentId)

    fun insertCategories(categories: List<Category>) = categoryDao.insertAll(categories)

    fun getAllCategories(): List<Category> = categoryDao.getAll()

    fun updateCategoryRandomSelectedFlag(categoryId: Int, randomSelected: Boolean) =
        categoryDao.updateRandomSelectedFlag(categoryId, randomSelected)
}