package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.CategoryWithPoemCount

@Dao
interface CategoryDao {
    @Transaction
    @Insert
    fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Delete
    fun delete(category: Category)

    @Update
    fun update(category: Category)

    @Query(
        """
            SELECT id from categories
                WHERE poet_id = :poetId
                    AND parent_id = 0
        """
    )
    fun getPoetCategoryId(poetId: Int): Int

    @Query(
        """
            SELECT * FROM categories
                WHERE id = :id
        """
    )
    fun getCategoryById(id: Int): Category

    @Query(
        """
        WITH RECURSIVE category_tree AS (
            SELECT * from categories WHERE id = :categoryId
            UNION ALL
            SELECT c.* FROM categories c JOIN category_tree ct ON c.id = ct.parent_id
        )
        SELECT * FROM category_tree ORDER BY id ASC
    """
    )
    fun getAllParentsOfCategoryId(categoryId: Int): List<Category>

    @Query(
        """
            UPDATE categories
                SET random_selected = :randomSelected
                    WHERE id = :categoryId
        """
    )
    fun updateRandomSelectedFlag(categoryId: Int, randomSelected: Boolean)

    @Query(
        """
        SELECT
            c.id as category_id,
            c.text as category_text,
            c.parent_id as category_parent_id,
            c.poet_id as category_poet_id,
            c.random_selected as category_random_selected,
            (SELECT COUNT(*) 
                FROM poems p 
                WHERE p.category_id IN (
                    WITH RECURSIVE sub_tree(id) AS (
                        SELECT id FROM categories WHERE id = c.id
                        UNION ALL
                        SELECT sc.id FROM categories sc JOIN sub_tree st ON sc.parent_id = st.id
                    )
                    SELECT id FROM sub_tree
                )
            ) as poem_count
        FROM categories c
        WHERE c.poet_id = :poetId AND c.parent_id = :parentId
        """
    )
    fun getCategoriesByPoetIdFilteredByParentIdWithPoemCount(
        poetId: Int,
        parentId: Int
    ): List<CategoryWithPoemCount>
}