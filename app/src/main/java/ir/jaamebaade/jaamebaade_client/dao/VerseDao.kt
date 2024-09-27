package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoryPoet
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights

@Dao
interface VerseDao {
    @Insert
    fun insertAll(verses: List<Verse>)

    @Query("SELECT * FROM verses")
    fun getAll(): List<Verse>

    @Delete
    fun delete(verse: Verse)

    @Update
    fun update(verse: Verse)

    @Query("SELECT * FROM verses WHERE poem_id = :poemId ORDER BY verse_order")
    fun getPoemVerses(poemId: Int): List<Verse>

    @Transaction
    @Query("SELECT * FROM verses WHERE poem_id = :poemId ORDER BY verse_order")
    fun getPoemVersesWithHighlights(poemId: Int): List<VerseWithHighlights>

    @Query(
        """
            SELECT 
                v.id AS verse_id, 
                v.text AS verse_text, 
                v.poem_id AS verse_poem_id, 
                v.verse_order AS verse_verse_order,
                v.position AS verse_position,
                p.id AS poem_id, 
                p.title AS poem_title, 
                p.category_id AS poem_category_id, 
                c.id AS category_id, 
                c.text AS category_text, 
                c.poet_id AS category_poet_id,
                c.parent_id AS category_parent_id,
                c.random_selected AS category_random_selected,
                pt.id AS poet_id, 
                pt.name AS poet_name,
                pt.description AS poet_description,
                pt.imageUrl AS poet_imageUrl
            FROM verses v
            JOIN poems p ON v.poem_id = p.id
            JOIN categories c ON p.category_id = c.id
            JOIN poets pt ON c.poet_id = pt.id
            WHERE (:poetId is null OR c.poet_id = :poetId)
                AND v.text LIKE :query
        """
    )
    fun searchVerses(query: String, poetId: Int?): List<VersePoemCategoryPoet>

    @Query("SELECT * FROM verses WHERE id = :verseId")
    fun getVerseById(verseId: Int): Verse

    @Transaction
    @Query("SELECT * FROM verses WHERE id = :verseId")
    fun getVerseWithHighlights(verseId: Int): VerseWithHighlights
}