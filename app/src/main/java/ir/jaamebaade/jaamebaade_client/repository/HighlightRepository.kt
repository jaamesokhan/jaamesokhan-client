package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Highlight
import javax.inject.Inject

class HighlightRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val highlightDao = db.highlightDao()
    fun insertHighlight(highlight: Highlight): Long = highlightDao.insertHighlight(highlight)
    fun updateHighlights(highlights: List<Highlight>) = highlightDao.updateHighlights(highlights)
    fun deleteHighlight(highlight: Highlight) = highlightDao.deleteHighlight(highlight)
    fun deleteHighlights(highlights: List<Highlight>) = highlightDao.deleteHighlights(highlights)
    fun getAllHighlightsWithVersePoemPoet() = highlightDao.getHighlightsWithVersePoemPoet()
}