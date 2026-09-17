package ir.jaamebaade.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ir.jaamebaade.jaamebaade_client.model.BookmarkLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.BookmarkWithLabels
import ir.jaamebaade.jaamebaade_client.model.LabelWithCount

@Dao
interface BookmarkLabelCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun assign(crossRef: BookmarkLabelCrossRef)

    @Query("DELETE FROM bookmark_label_cross_refs WHERE bookmark_id = :bookmarkId AND label_id = :labelId")
    fun unassign(bookmarkId: Int, labelId: Int)

    @Transaction
    @Query("SELECT * FROM bookmarks")
    fun getBookmarksWithLabels(): List<BookmarkWithLabels>

    @Query(
        """
        SELECT labels.*, COUNT(bookmark_label_cross_refs.bookmark_id) AS item_count
        FROM labels
        LEFT JOIN bookmark_label_cross_refs ON labels.id = bookmark_label_cross_refs.label_id
        WHERE labels.type = 'bookmark'
        GROUP BY labels.id
        ORDER BY labels.created_at ASC
        """
    )
    fun getLabelCounts(): List<LabelWithCount>
}
