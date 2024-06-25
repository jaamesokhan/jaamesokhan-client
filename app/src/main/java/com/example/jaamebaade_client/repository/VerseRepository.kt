package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Verse
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun getAllVerses() = verseDao.getAll()

    fun insertVerse(verse: Verse) = verseDao.insertAll(verse)

    fun deleteVerse(verse: Verse) = verseDao.delete(verse)

    fun updateVerse(verse: Verse) = verseDao.update(verse)

    fun getPoemVerses(poemId: Int): List<Verse> = verseDao.getPoemVerses(poemId)

    fun getVerseById(verseId: Int): Verse? = verseDao.getVerseById(verseId)

    fun getVerseWithHighlights(verseId: Int) = verseDao.getVerseWithHighlights(verseId)

    fun searchVerses(query: String, poetId: Int?): List<Verse> = verseDao.searchVerses("%${query}%", poetId)
}