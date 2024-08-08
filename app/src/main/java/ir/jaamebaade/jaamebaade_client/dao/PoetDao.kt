package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Poet

@Dao
interface PoetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg poet: Poet)

    @Query("SELECT * FROM poets")
    fun getAll(): List<Poet>

    @Delete
    fun deletePoets(poets: List<Poet>)

    @Update
    fun update(poet: Poet)

    @Query("SELECT * FROM poets WHERE id = :poetId")
    fun getPoetById(poetId: Int): Poet

    @Query(
        """
        SELECT poets.* FROM poets
        INNER JOIN categories ON poets.id = categories.poet_id
        INNER JOIN poems ON categories.id = poems.category_id
        WHERE poems.id = :poemId
    """
    )
    fun getPoetByPoemId(poemId: Int): Poet

}