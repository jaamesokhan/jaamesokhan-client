package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoryPoet
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun getPoemVersesWithHighlights(poemId: Int) = verseDao.getPoemVersesWithHighlights(poemId)

    fun searchVerses(query: String, poetId: Int?): List<VersePoemCategoryPoet> =
        verseDao.searchVerses("%${query}%", poetId)

    fun insertVerses(verses: List<Verse>) = verseDao.insertAll(verses)
}