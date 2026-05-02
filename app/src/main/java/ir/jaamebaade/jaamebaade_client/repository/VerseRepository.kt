package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoryPoet
import ir.jaamebaade.jaamebaade_client.utility.normalizedForSearch
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun getPoemVersesWithHighlights(poemId: Int) = verseDao.getPoemVersesWithHighlights(poemId)

    fun searchVerses(query: String, poetIds: List<Int>): List<VersePoemCategoryPoet> {
        val normalizedQuery = "%${query.normalizedForSearch()}%"
        return if (poetIds.isEmpty()) {
            verseDao.searchVerses(normalizedQuery)
        } else {
            verseDao.searchVerses(normalizedQuery, poetIds)
        }
    }

    fun insertVerses(verses: List<Verse>) =
        verseDao.insertAll(verses.map { it.copy(normalizedText = it.text.normalizedForSearch()) })

    fun getFirst4VersesByPoemId(poemId: Int) = verseDao.getFirst4VersesByPoemId(poemId)
}
