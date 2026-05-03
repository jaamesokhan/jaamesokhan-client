package ir.jaamebaade.jaamebaade_client.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
import ir.jaamebaade.jaamebaade_client.utility.normalizedForSearch

@Database(
    entities = [Poet::class, Category::class, Poem::class,
        Verse::class, Highlight::class, Bookmark::class, Comment::class,
        HistoryRecord::class, SearchHistoryRecord::class],
    version = 8,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
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

    companion object {
        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE verses ADD COLUMN normalized_text TEXT NOT NULL DEFAULT ''")

                val cursor = db.query("SELECT id, text FROM verses")
                val updateStatement = db.compileStatement(
                    "UPDATE verses SET normalized_text = ? WHERE id = ?"
                )
                try {
                    val idIndex = cursor.getColumnIndexOrThrow("id")
                    val textIndex = cursor.getColumnIndexOrThrow("text")
                    while (cursor.moveToNext()) {
                        updateStatement.bindString(1, cursor.getString(textIndex).normalizedForSearch())
                        updateStatement.bindLong(2, cursor.getLong(idIndex))
                        updateStatement.executeUpdateDelete()
                        updateStatement.clearBindings()
                    }
                } finally {
                    cursor.close()
                }
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val cursor = db.query("SELECT id, text FROM verses")
                val updateStatement = db.compileStatement(
                    "UPDATE verses SET normalized_text = ? WHERE id = ?"
                )

                try {
                    val idIndex = cursor.getColumnIndexOrThrow("id")
                    val textIndex = cursor.getColumnIndexOrThrow("text")
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idIndex)
                        val text = cursor.getString(textIndex)
                        updateStatement.bindString(1, text.normalizedForSearch())
                        updateStatement.bindLong(2, id)
                        updateStatement.executeUpdateDelete()
                        updateStatement.clearBindings()
                    }
                } finally {
                    cursor.close()
                }
            }
        }
    }
}
