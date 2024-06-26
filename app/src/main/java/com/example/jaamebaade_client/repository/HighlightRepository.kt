package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Highlight
import javax.inject.Inject

class HighlightRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val highlightDao = db.highlightDao()
    fun insertHighlight(highlight: Highlight) = highlightDao.insertHighlight(highlight)
}