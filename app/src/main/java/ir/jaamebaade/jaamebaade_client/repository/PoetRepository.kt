package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Poet
import javax.inject.Inject

class PoetRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poetDao = db.poetDao()

    fun getAllPoets(): List<Poet> = poetDao.getAll()

    fun insertPoet(poet: Poet) = poetDao.insertAll(poet)

    fun getPoetById(poetId: Int): Poet = poetDao.getPoetById(poetId)

    fun deletePoets(poets: List<Poet>) = poetDao.deletePoets(poets)

    fun updatePoetDownloadStatus(poet: Poet) = poetDao.update(poet)
}