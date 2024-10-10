package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemPoet

@Dao
interface HighlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighlight(highlight: Highlight)

    @Query("SELECT * FROM highlights")
    fun getAll(): List<Highlight>

    @Query("SELECT * FROM highlights WHERE verse_id = :verseId")
    fun getHighlightsForVerse(verseId: Int): List<Highlight>

    @Delete
    fun deleteHighlight(highlight: Highlight)

    @Query(
        """
            SELECT 
                hg.id AS highlight_id,
                hg.start_index AS highlight_start_index,
                hg.end_index AS highlight_end_index,
                hg.verse_id  AS highlight_verse_id,
                hg.created_at AS highlight_created_at,
                v.id AS verse_id, 
                v.text AS verse_text, 
                v.poem_id AS verse_poem_id, 
                v.verse_order AS verse_verse_order,
                v.position AS verse_position,
                p.id AS poem_id, 
                p.title AS poem_title, 
                p.category_id AS poem_category_id,
                pt.id AS poet_id, 
                pt.name AS poet_name,
                pt.description AS poet_description,
                pt.imageUrl AS poet_imageUrl
            FROM highlights hg
            JOIN verses v ON hg.verse_id = v.id
            JOIN poems p ON v.poem_id = p.id
            JOIN categories c ON p.category_id = c.id
            JOIN poets pt ON c.poet_id = pt.id
            ORDER BY highlight_created_at ASC
        """
    )
    fun getHighlightsWithVersePoemPoet(): List<HighlightVersePoemPoet>
}