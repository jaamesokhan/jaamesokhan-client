package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemPoet
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

    fun getAllBookmarksWithPoemAndPoet(): List<BookmarkPoemPoet> {
        return bookmarkDao.getAllBookMarksWithPoemAndPoet()
    }

    fun deleteBookmark(bookmark: Bookmark) {
        bookmarkDao.removeBookmark(bookmark.poemId)
    }
}