package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

class CategoryWithPoemCount(
    @Embedded(prefix = "category_")
    val category: Category,

    @ColumnInfo(name = "poem_count")
    val poemCount: Int,
)