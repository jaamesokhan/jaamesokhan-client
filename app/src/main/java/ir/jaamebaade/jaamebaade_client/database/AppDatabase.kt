package ir.jaamebaade.jaamebaade_client.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.jaamebaade.jaamebaade_client.dao.BookmarkDao
import ir.jaamebaade.jaamebaade_client.dao.BookmarkLabelCrossRefDao
import ir.jaamebaade.jaamebaade_client.dao.CategoryDao
import ir.jaamebaade.jaamebaade_client.dao.CommentDao
import ir.jaamebaade.jaamebaade_client.dao.HighlightDao
import ir.jaamebaade.jaamebaade_client.dao.HighlightLabelCrossRefDao
import ir.jaamebaade.jaamebaade_client.dao.HistoryItemDao
import ir.jaamebaade.jaamebaade_client.dao.LabelDao
import ir.jaamebaade.jaamebaade_client.dao.PoemDao
import ir.jaamebaade.jaamebaade_client.dao.PoetDao
import ir.jaamebaade.jaamebaade_client.dao.SearchHistoryDao
import ir.jaamebaade.jaamebaade_client.dao.VerseDao
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.BookmarkLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.model.VerseSearch
import ir.jaamebaade.jaamebaade_client.utility.normalizedForSearch
import java.util.UUID

@Database(
    entities = [Poet::class, Category::class, Poem::class,
        Verse::class, Highlight::class, Bookmark::class, Comment::class,
        HistoryRecord::class, SearchHistoryRecord::class, VerseSearch::class,
        Label::class, BookmarkLabelCrossRef::class, HighlightLabelCrossRef::class],
    version = 12,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
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
    abstract fun labelDao(): LabelDao
    abstract fun bookmarkLabelCrossRefDao(): BookmarkLabelCrossRefDao
    abstract fun highlightLabelCrossRefDao(): HighlightLabelCrossRefDao

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

        // Adds the verses_fts FTS4 table (external content over `verses`) so search can use
        // MATCH instead of a leading-wildcard LIKE full scan. normalized_text on `verses` is
        // already correct as of MIGRATION_7_8, so this migration only adds the FTS index and
        // backfills it; it does not touch `verses` itself.
        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE VIRTUAL TABLE IF NOT EXISTS `verses_fts` USING FTS4(`normalized_text` TEXT NOT NULL, content=`verses`)"
                )

                // Content-sync triggers Room expects for an external-content FTS4 table
                // (must match what Room's schema validator generates for this entity).
                db.execSQL(
                    "CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_verses_fts_BEFORE_UPDATE BEFORE UPDATE ON `verses` BEGIN DELETE FROM `verses_fts` WHERE `docid`=OLD.`rowid`; END"
                )
                db.execSQL(
                    "CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_verses_fts_BEFORE_DELETE BEFORE DELETE ON `verses` BEGIN DELETE FROM `verses_fts` WHERE `docid`=OLD.`rowid`; END"
                )
                db.execSQL(
                    "CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_verses_fts_AFTER_UPDATE AFTER UPDATE ON `verses` BEGIN INSERT INTO `verses_fts`(`docid`, `normalized_text`) VALUES (NEW.`rowid`, NEW.`normalized_text`); END"
                )
                db.execSQL(
                    "CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_verses_fts_AFTER_INSERT AFTER INSERT ON `verses` BEGIN INSERT INTO `verses_fts`(`docid`, `normalized_text`) VALUES (NEW.`rowid`, NEW.`normalized_text`); END"
                )

                // Backfill: triggers only cover future writes, existing rows need this once.
                db.execSQL(
                    "INSERT INTO `verses_fts`(`docid`, `normalized_text`) SELECT `rowid`, `normalized_text` FROM `verses`"
                )
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `labels` (
                        `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        `name` TEXT NOT NULL,
                        `color` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `created_at` INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `bookmark_label_cross_refs` (
                        `bookmark_id` INTEGER NOT NULL,
                        `label_id` INTEGER NOT NULL,
                        PRIMARY KEY(`bookmark_id`, `label_id`),
                        FOREIGN KEY(`bookmark_id`) REFERENCES `bookmarks`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`label_id`) REFERENCES `labels`(`id`) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmark_label_cross_refs_bookmark_id` ON `bookmark_label_cross_refs` (`bookmark_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmark_label_cross_refs_label_id` ON `bookmark_label_cross_refs` (`label_id`)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `highlight_label_cross_refs` (
                        `highlight_id` INTEGER NOT NULL,
                        `label_id` INTEGER NOT NULL,
                        PRIMARY KEY(`highlight_id`, `label_id`),
                        FOREIGN KEY(`highlight_id`) REFERENCES `highlights`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`label_id`) REFERENCES `labels`(`id`) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_highlight_label_cross_refs_highlight_id` ON `highlight_label_cross_refs` (`highlight_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_highlight_label_cross_refs_label_id` ON `highlight_label_cross_refs` (`label_id`)")
            }
        }

        // Adds highlights.group_id: rows inserted by one PoemViewModel.highlight() call (a
        // single multi-verse selection) now share an explicit id, so grouping for
        // recolor/remove/merge no longer has to guess from verse-id adjacency, which
        // couldn't distinguish one real multi-verse highlight from two unrelated highlights
        // that happened to land on neighboring verses.
        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE highlights ADD COLUMN group_id TEXT NOT NULL DEFAULT ''")

                // Best-effort reconstruction for existing rows: a run of same-color highlights
                // on consecutive verse ids within the same poem was the old (buggy) definition
                // of "one logical highlight", so it's the closest available signal for what
                // was actually created together before this migration.
                val cursor = db.query(
                    """
                    SELECT hg.id AS id, hg.color AS color, v.poem_id AS poem_id, v.id AS verse_id
                    FROM highlights hg
                    JOIN verses v ON hg.verse_id = v.id
                    ORDER BY v.poem_id ASC, v.id ASC
                    """.trimIndent()
                )
                val updateStatement = db.compileStatement("UPDATE highlights SET group_id = ? WHERE id = ?")
                try {
                    val idIndex = cursor.getColumnIndexOrThrow("id")
                    val colorIndex = cursor.getColumnIndexOrThrow("color")
                    val poemIdIndex = cursor.getColumnIndexOrThrow("poem_id")
                    val verseIdIndex = cursor.getColumnIndexOrThrow("verse_id")

                    var groupId = ""
                    var prevPoemId: Long? = null
                    var prevVerseId: Long? = null
                    var prevColor: Long? = null

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idIndex)
                        val color = cursor.getLong(colorIndex)
                        val poemId = cursor.getLong(poemIdIndex)
                        val verseId = cursor.getLong(verseIdIndex)

                        val continuesGroup = prevPoemId == poemId && prevVerseId != null &&
                            verseId == prevVerseId + 1 && prevColor == color
                        if (!continuesGroup) {
                            groupId = UUID.randomUUID().toString()
                        }

                        updateStatement.bindString(1, groupId)
                        updateStatement.bindLong(2, id)
                        updateStatement.executeUpdateDelete()
                        updateStatement.clearBindings()

                        prevPoemId = poemId
                        prevVerseId = verseId
                        prevColor = color
                    }
                } finally {
                    cursor.close()
                }
            }
        }
    }
}
