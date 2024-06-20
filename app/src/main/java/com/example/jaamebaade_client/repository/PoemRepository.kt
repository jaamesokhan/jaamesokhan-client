package com.example.jaamebaade_client.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Poem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PoemRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poemDao = db.poemDao()

    fun getAllPoems() = poemDao.getAll()

    fun insertPoem(poem: Poem) = poemDao.insertAll(poem)

    fun deletePoem(poem: Poem) = poemDao.delete(poem)

    fun updatePoem(poem: Poem) = poemDao.update(poem)

    fun getPoemsByCategoryIdPaged(categoryId: Int): Flow<PagingData<Poem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { poemDao.getPoemsByCategory(categoryId) }
        ).flow
    }

    fun getPoemsByCategory(categoryId: Int): List<Poem> = poemDao.getAllPoemsByCategory(categoryId)
}
