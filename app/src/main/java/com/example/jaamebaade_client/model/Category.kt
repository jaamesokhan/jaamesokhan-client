package com.example.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = Poet::class,
        parentColumns = ["id"],
        childColumns = ["poet_id"],
        onDelete = ForeignKey.CASCADE
    )]
    )
data class Category (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name= "parent_id") val parentId: Int,
    @ColumnInfo(name = "poet_id") val poetId: Int
)