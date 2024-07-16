package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.model.VersePoemCategoryPoet
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun insertVerse(verse: Verse) = verseDao.insertAll(verse)

    fun getPoemVersesWithHighlights(poemId: Int) = verseDao.getPoemVersesWithHighlights(poemId)

    fun searchVerses(query: String, poetId: Int?): List<VersePoemCategoryPoet> =
        verseDao.searchVerses("%${query}%", poetId)
}