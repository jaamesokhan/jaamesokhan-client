package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Highlight
import javax.inject.Inject

class HighlightRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val highlightDao = db.highlightDao()
    fun insertHighlight(highlight: Highlight) = highlightDao.insertHighlight(highlight)
    fun deleteHighlight(highlight: Highlight) = highlightDao.deleteHighlight(highlight)
    fun getAllHighlightsWithVersePoemPoet() = highlightDao.getHighlightsWithVersePoemPoet()
}