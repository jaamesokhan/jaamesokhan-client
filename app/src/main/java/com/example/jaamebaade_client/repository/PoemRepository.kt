package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Poem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PoemRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poemDao = db.poemDao()

    fun getAllPoems() = poemDao.getAll()

    fun insertPoem(poem: Poem) = poemDao.insertAll(poem)

    fun deletePoem(poem: Poem) = poemDao.delete(poem)

    fun updatePoem(poem: Poem) = poemDao.update(poem)

}