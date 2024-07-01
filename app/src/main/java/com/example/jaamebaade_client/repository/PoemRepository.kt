package com.example.jaamebaade_client.repository

import androidx.paging.PagingSource
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Pair
import com.example.jaamebaade_client.model.Poem
import javax.inject.Inject

class PoemRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poemDao = db.poemDao()

    fun getAllPoems() = poemDao.getAll()

    fun insertPoem(poem: Poem) = poemDao.insertAll(poem)

    fun deletePoem(poem: Poem) = poemDao.delete(poem)

    fun updatePoem(poem: Poem) = poemDao.update(poem)


    fun getPoemPagingSource(categoryId: Int): PagingSource<Int, Poem> {
        return poemDao.getPoemPagingSource(categoryId)
    }

    fun getPoemById(id: Int): Poem {
        return poemDao.getPoemById(id)
    }

    fun getFirstAndLastWithCategoryId(categoryId: Int): Pair =
        poemDao.getFirstAndLastWithCategoryId(categoryId)

    fun getCategoryIdByPoemId(poemId: Int): Int = poemDao.getCategoryByPoemId(poemId)

}
