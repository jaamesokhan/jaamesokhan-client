package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.model.Bookmark
import com.example.jaamebaade_client.model.BookmarkPoemPoet
import javax.inject.Inject

class BookmarkRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val bookmarkDao = db.bookmarkDao()
    fun insertBookmark(poemId: Int) {
        val bookmark = Bookmark(poemId = poemId)
        bookmarkDao.insertBookmark(bookmark)
    }

    fun isPoemBookmarked(poemId: Int): Boolean {
        return bookmarkDao.isPoemLiked(poemId)

    }

    fun removeBookmark(poemId: Int) {
        bookmarkDao.removeBookmark(poemId)
    }

    fun getAllBookmarks(): List<Bookmark> {
        return bookmarkDao.getAllBookmarks()
    }

    fun getAllBookmarksWithPoemAndPoet(): List<BookmarkPoemPoet> {
        return bookmarkDao.getAllBookMarksWithPoemAndPoet()
    }
}