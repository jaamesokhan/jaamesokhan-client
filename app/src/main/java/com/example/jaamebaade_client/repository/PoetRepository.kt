package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Poet
import javax.inject.Inject

class PoetRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poetDao = db.poetDao()

    fun getAllPoets(): List<Poet> = poetDao.getAll()

    fun insertPoet(poet: Poet) = poetDao.insertAll(poet)

    fun getPoetById(poetId: Int): Poet = poetDao.getPoetById(poetId)
}