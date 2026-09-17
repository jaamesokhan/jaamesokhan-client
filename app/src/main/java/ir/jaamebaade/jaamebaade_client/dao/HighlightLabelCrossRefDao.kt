package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ir.jaamebaade.jaamebaade_client.model.HighlightLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.HighlightWithLabels
import ir.jaamebaade.jaamebaade_client.model.LabelWithCount

@Dao
interface HighlightLabelCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun assign(crossRef: HighlightLabelCrossRef)

    @Query("DELETE FROM highlight_label_cross_refs WHERE highlight_id = :highlightId AND label_id = :labelId")
    fun unassign(highlightId: Int, labelId: Int)

    @Transaction
    @Query("SELECT * FROM highlights")
    fun getHighlightsWithLabels(): List<HighlightWithLabels>

    @Query(
        """
        SELECT labels.*, COUNT(highlight_label_cross_refs.highlight_id) AS item_count
        FROM labels
        LEFT JOIN highlight_label_cross_refs ON labels.id = highlight_label_cross_refs.label_id
        WHERE labels.type = 'highlight'
        GROUP BY labels.id
        ORDER BY labels.created_at ASC
        """
    )
    fun getLabelCounts(): List<LabelWithCount>
}
