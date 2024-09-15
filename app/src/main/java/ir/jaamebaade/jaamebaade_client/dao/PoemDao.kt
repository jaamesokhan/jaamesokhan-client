package ir.jaamebaade.jaamebaade_client.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Pair
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.PoemWithFirstVerse
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet

@Dao
interface PoemDao {

    @Transaction
    @Insert
    fun insertAll(vararg poem: Poem)

    @Transaction
    @Query("SELECT * FROM poems")
    fun getAll(): List<Poem>

    @Transaction
    @Delete
    fun delete(poem: Poem)

    @Transaction
    @Update
    fun update(poem: Poem)

    @Query("SELECT * FROM poems WHERE category_id = :categoryId")
    fun getPoemsByCategory(categoryId: Int): List<Poem>

    @Query(
        """
            SELECT
                p.id AS poem_id,
                p.title AS poem_title,
                p.category_id AS poem_category_id,
                v.id AS verse_id,
                v.text AS verse_text,
                v.verse_order AS verse_verse_order,
                v.position AS verse_position,
                v.poem_id AS verse_poem_id
            FROM poems p
            JOIN verses v ON v.poem_id = p.id
            WHERE p.category_id = :categoryId 
            AND v.verse_order = 1
        """
    )
    fun getPoemPagingSource(categoryId: Int): PagingSource<Int, PoemWithFirstVerse>

    @Query("SELECT MIN(id) as first, MAX(id) as second FROM poems WHERE category_id = :categoryId")
    fun getFirstAndLastWithCategoryId(categoryId: Int): Pair


    @Query("SELECT * FROM poems WHERE id = :id")
    fun getPoemById(id: Int): Poem

    @Query("SELECT category_id FROM poems WHERE id = :poemId")

    fun getCategoryByPoemId(poemId: Int): Int

    @Query(
        """
        WITH RECURSIVE category_tree AS (
        SELECT id from categories WHERE id = :categoryId
        UNION ALL
        SELECT c.id FROM categories c JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT
        pm.id AS poem_id,
        pm.title AS poem_title,
        pm.category_id AS poem_category_id,
        pt.id AS poet_id,
        pt.name AS poet_name,
        pt.description AS poet_description,
        pt.imageUrl AS poet_imageUrl
        FROM poems pm
        JOIN categories c ON c.id = pm.category_id
        JOIN poets pt ON pt.id = c.poet_id
        WHERE (:categoryId IS NULL OR c.id IN category_tree)
        ORDER BY RANDOM()
        LIMIT 1
    """
    )
    fun getRandomPoem(categoryId: Int?): PoemWithPoet?

    @Query(
        """
        SELECT 
        pm.id AS poem_id,
        pm.title AS poem_title,
        pm.category_id AS poem_category_id,
        pt.id AS poet_id,
        pt.name AS poet_name,
        pt.description AS poet_description,
        pt.imageUrl AS poet_imageUrl
        FROM poems pm
        JOIN categories c ON c.id = pm.category_id
        JOIN poets pt ON pt.id = c.poet_id
        WHERE pm.id = :poemId 
    """
    )
    fun getPoemWithPoet(poemId: Int): PoemWithPoet
}