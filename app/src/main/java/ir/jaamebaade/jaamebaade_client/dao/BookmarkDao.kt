package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemPoet

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT COUNT(*) FROM bookmarks WHERE poem_id = :poemId")
    fun isPoemLiked(poemId: Int): Boolean

    @Query("DELETE FROM bookmarks WHERE poem_id = :poemId")
    fun removeBookmark(poemId: Int)

    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): List<Bookmark>

    @Query(
        """
        SELECT
            bookmarks.id AS bookmark_id, 
            bookmarks.poem_id AS bookmark_poem_id,
            bookmarks.created_at AS bookmark_created_at,
            poems.id AS poem_id, 
            poems.title AS poem_title, 
            poems.category_id AS poem_category_id, 
            poets.id AS poet_id, 
            poets.name AS poet_name,
            poets.description AS poet_description,
            poets.imageUrl AS poet_imageUrl
        FROM bookmarks
        INNER JOIN poems ON bookmarks.poem_id = poems.id
        INNER JOIN categories on poems.category_id = categories.id
        INNER JOIN poets ON categories.poet_id = poets.id
        ORDER BY bookmark_created_at ASC
    """
    )
    fun getAllBookMarksWithPoemAndPoet(): List<BookmarkPoemPoet>
}