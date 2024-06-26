package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Poet
import javax.inject.Inject

class PoetRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poetDao = db.poetDao()

    fun getAllPoets(): List<Poet> = poetDao.getAll()

    fun insertPoet(poet: Poet) = poetDao.insertAll(poet)

    fun deletePoet(poet: Poet) = poetDao.delete(poet)

    fun updatePoet(poet: Poet) = poetDao.update(poet)

    fun insetPoets(poets: List<Poet>) = poetDao.insertAll(*poets.toTypedArray())

    fun getPoetById(poetId: Int): Poet = poetDao.getPoetById(poetId)

    fun getPoetByPoemId(poemId: Int): Poet = poetDao.getPoetByPoemId(poemId)
}