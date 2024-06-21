package com.example.jaamebaade_client.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jaamebaade_client.model.Bookmark
import com.example.jaamebaade_client.model.Highlight

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT COUNT(*) FROM bookmarks WHERE poem_id = :poemId")
    fun isPoemLiked(poemId: Int): Boolean

    @Query("DELETE FROM bookmarks WHERE poem_id = :poemId")
    fun removeBookmark(poemId: Int)
}