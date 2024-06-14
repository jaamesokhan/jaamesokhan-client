package com.example.jaamebaade_client.repository

import android.content.Context
import com.example.jaamebaade_client.database.DatabaseBuilder
import com.example.jaamebaade_client.model.Verse

class VerseRepository (context: Context){
    private val db = DatabaseBuilder.getInstance(context)
    private val verseDao = db.verseDao()

    fun getAllVerses() = verseDao.getAll()

    fun insertVerse(verse: Verse) = verseDao.insertAll(verse)

    fun deleteVerse(verse: Verse) = verseDao.delete(verse)

    fun updateVerse(verse: Verse) = verseDao.update(verse)
}