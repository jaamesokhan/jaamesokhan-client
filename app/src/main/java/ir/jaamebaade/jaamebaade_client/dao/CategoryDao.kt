package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Category

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
        """SELECT * FROM categories 
            WHERE poet_id = :poetId 
                AND parent_id = :parentId
        """
    )
    fun getCategoriesByPoetIdFilteredByParentId(poetId: Int, parentId: Int): List<Category>

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
}