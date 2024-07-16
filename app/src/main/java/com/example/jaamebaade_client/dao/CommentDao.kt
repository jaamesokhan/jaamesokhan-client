package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jaamebaade_client.model.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertComment(comment: Comment)

    @Query("SELECT * FROM comments")
    fun getAll(): List<Comment>

    @Query("SELECT * FROM comments WHERE poem_id = :poemId")
    fun getCommentsForPoem(poemId: Int): List<Comment>
    @Delete
    fun deleteComment(comment: Comment)
}