package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Verse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VerseRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val verseDao = db.verseDao()

    fun getAllVerses() = verseDao.getAll()

    fun insertVerse(verse: Verse) = verseDao.insertAll(verse)

    fun deleteVerse(verse: Verse) = verseDao.delete(verse)

    fun updateVerse(verse: Verse) = verseDao.update(verse)

    fun getPoemVerses(poemId: Int): List<Verse> = verseDao.getPoemVerses(poemId)
}