package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Poet

class PoetRepository(context: Context) {
    private val db = DatabaseBuilder.getInstance(context)
    private val poetDao = db.poetDao()

    fun getAllPoets(): List<Poet> = poetDao.getAll()

    fun insertPoet(poet: Poet) = poetDao.insertAll(poet)

    fun deletePoet(poet: Poet) = poetDao.delete(poet)

    fun updatePoet(poet: Poet) = poetDao.update(poet)
}