package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.model.CommentPoemPoet

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertComment(comment: Comment): Long

    @Query("SELECT * FROM comments")
    fun getAll(): List<Comment>

    @Query("SELECT * FROM comments WHERE poem_id = :poemId")
    fun getCommentsForPoem(poemId: Int): List<Comment>

    @Delete
    fun deleteComment(comment: Comment)

    @Query(
        """
            SELECT 
                cm.id AS comment_id,
                cm.poem_id AS comment_poem_id,
                cm.text AS comment_text,
                cm.created_at AS comment_created_at,
                p.id AS poem_id, 
                p.title AS poem_title, 
                p.category_id AS poem_category_id,
                pt.id AS poet_id, 
                pt.name AS poet_name,
                pt.description AS poet_description,
                pt.imageUrl AS poet_imageUrl
            FROM comments cm
            JOIN poems p ON cm.poem_id = p.id
            JOIN categories c ON p.category_id = c.id
            JOIN poets pt ON c.poet_id = pt.id
        """
    )
    fun getCommentsWithPoemPoet(): List<CommentPoemPoet>
}