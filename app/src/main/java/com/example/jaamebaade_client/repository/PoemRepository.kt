package com.example.jaamebaade_client.repository

import androidx.paging.PagingSource
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Pair
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.PoemWithPoet
import javax.inject.Inject

class PoemRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poemDao = db.poemDao()

    fun insertPoem(poem: Poem) = poemDao.insertAll(poem)

    fun getPoemPagingSource(categoryId: Int): PagingSource<Int, Poem> {
        return poemDao.getPoemPagingSource(categoryId)
    }

    fun getPoemById(id: Int): Poem {
        return poemDao.getPoemById(id)
    }

    fun getFirstAndLastWithCategoryId(categoryId: Int): Pair =
        poemDao.getFirstAndLastWithCategoryId(categoryId)

    fun getCategoryIdByPoemId(poemId: Int): Int = poemDao.getCategoryByPoemId(poemId)

    fun getRandomPoem(categoryId: Int? = null): PoemWithPoet = poemDao.getRandomPoem(categoryId)

    fun getPoemWithPoet(poemId: Int): PoemWithPoet = poemDao.getPoemWithPoet(poemId)
}
