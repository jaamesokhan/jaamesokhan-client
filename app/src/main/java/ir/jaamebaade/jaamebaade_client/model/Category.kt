package ir.jaamebaade.jaamebaade_client.model

import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = Poet::class,
        parentColumns = ["id"],
        childColumns = ["poet_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "parent_id") val parentId: Int,
    @ColumnInfo(name = "poet_id") val poetId: Int,
    @ColumnInfo(name = "random_selected") var randomSelected: Boolean? = true,
) {
    fun toGraphNode() = CategoryGraphNode(category = this, isSelectedForRandom =  mutableStateOf(this.randomSelected ?: true))
}