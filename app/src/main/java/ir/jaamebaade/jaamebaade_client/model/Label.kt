package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LabelType(val value: String) {
    BOOKMARK("bookmark"),
    HIGHLIGHT("highlight"),
}

val CATEGORY_COLOR_PALETTE = listOf(
    "#bad982", "#cce595", "#9acc3d", "#718053", "#cc3d3d", "#cccccc",
    "#ccb23d", "#cc7a3d", "#3dbdab", "#3d7fcc", "#8a3dcc", "#cc3d8a",
)

@Entity(tableName = "labels")
data class Label(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: String,
    val type: String,
    @ColumnInfo(name = "created_at", defaultValue = "0") val createdAt: Long = System.currentTimeMillis(),
)

data class LabelWithCount(
    @androidx.room.Embedded val label: Label,
    @ColumnInfo(name = "item_count") val itemCount: Int,
)
