package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Comment
import javax.inject.Inject

class CommentRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val commentDao = db.commentDao()
    fun insertComment(comment: Comment): Long = commentDao.insertComment(comment)
    fun getAllCommentsWithPoemPoet() = commentDao.getCommentsWithPoemPoet()
    fun getCommentsForPoem(poemId: Int) = commentDao.getCommentsForPoem(poemId)
    fun deleteComment(comment: Comment) = commentDao.deleteComment(comment)
}