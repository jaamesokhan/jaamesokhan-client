package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.jaamebaade.jaamebaade_client.model.Label

@Dao
interface LabelDao {
    @Insert
    fun insertLabel(label: Label): Long

    @Update
    fun updateLabel(label: Label)

    @Delete
    fun deleteLabel(label: Label)

    @Query("SELECT * FROM labels WHERE type = :type ORDER BY created_at ASC")
    fun getLabelsByType(type: String): List<Label>
}
