package com.example.jaamebaade_client.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jaamebaade_client.dao.CategoryDao
import com.example.jaamebaade_client.dao.PoemDao
import com.example.jaamebaade_client.dao.PoetDao
import com.example.jaamebaade_client.dao.VerseDao
import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.model.Verse

@Database(entities = [Poet::class, Category::class, Poem::class, Verse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun poetDao(): PoetDao

    abstract fun categoryDao(): CategoryDao
    abstract fun poemDao(): PoemDao

    abstract fun verseDao(): VerseDao


}