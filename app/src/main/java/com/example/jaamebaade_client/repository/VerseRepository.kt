package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.model.VersePoemCategoryPoet
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun getAllVerses() = verseDao.getAll()

    fun insertVerse(verse: Verse) = verseDao.insertAll(verse)

    fun deleteVerse(verse: Verse) = verseDao.delete(verse)

    fun updateVerse(verse: Verse) = verseDao.update(verse)

    fun getPoemVerses(poemId: Int): List<Verse> = verseDao.getPoemVerses(poemId)

    fun getPoemVersesWithHighlights(poemId: Int) = verseDao.getPoemVersesWithHighlights(poemId)

    fun getVerseById(verseId: Int): Verse? = verseDao.getVerseById(verseId)

    fun getVerseWithHighlights(verseId: Int) = verseDao.getVerseWithHighlights(verseId)

    fun searchVerses(query: String, poetId: Int?): List<VersePoemCategoryPoet> =
        verseDao.searchVerses("%${query}%", poetId)
}