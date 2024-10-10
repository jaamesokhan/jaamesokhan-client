package ir.jaamebaade.jaamebaade_client.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ir.jaamebaade.jaamebaade_client.dao.BookmarkDao
import ir.jaamebaade.jaamebaade_client.dao.CategoryDao
import ir.jaamebaade.jaamebaade_client.dao.CommentDao
import ir.jaamebaade.jaamebaade_client.dao.HighlightDao
import ir.jaamebaade.jaamebaade_client.dao.HistoryItemDao
import ir.jaamebaade.jaamebaade_client.dao.PoemDao
import ir.jaamebaade.jaamebaade_client.dao.PoetDao
import ir.jaamebaade.jaamebaade_client.dao.SearchHistoryDao
import ir.jaamebaade.jaamebaade_client.dao.VerseDao
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import ir.jaamebaade.jaamebaade_client.model.Verse

@Database(
    entities = [Poet::class, Category::class, Poem::class,
        Verse::class, Highlight::class, Bookmark::class, Comment::class,
        HistoryRecord::class, SearchHistoryRecord::class],
    version = 6,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun poetDao(): PoetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun poemDao(): PoemDao
    abstract fun verseDao(): VerseDao
    abstract fun highlightDao(): HighlightDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun commentDao(): CommentDao
    abstract fun historyDao(): HistoryItemDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
