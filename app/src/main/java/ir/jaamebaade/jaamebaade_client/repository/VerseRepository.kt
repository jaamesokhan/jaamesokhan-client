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
        val ftsQuery = buildFtsMatchQuery(query)
        if (ftsQuery.isBlank()) return emptyList()
        return if (poetIds.isEmpty()) {
            verseDao.searchVerses(ftsQuery)
        } else {
            verseDao.searchVerses(ftsQuery, poetIds)
        }
    }

    // Each token becomes a prefix match (token*), ANDed together: matches verses containing
    // all of the typed words (in any order), as a whole-token prefix. Unlike the old
    // LIKE '%query%' this won't match a query landing mid-word.
    private fun buildFtsMatchQuery(query: String): String {
        return query.normalizedForSearch()
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .joinToString(" ") { "$it*" }
    }

    fun insertVerses(verses: List<Verse>) =
        verseDao.insertAll(verses.map { it.copy(normalizedText = it.text.normalizedForSearch()) })

    fun getFirst4VersesByPoemId(poemId: Int) = verseDao.getFirst4VersesByPoemId(poemId)
}
