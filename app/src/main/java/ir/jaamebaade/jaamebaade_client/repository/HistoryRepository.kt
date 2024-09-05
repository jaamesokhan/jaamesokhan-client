package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.HistoryItem
import javax.inject.Inject


class HistoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val historyDao = db.historyDao()
    fun insertHistoryItem(historyItem: HistoryItem) = historyDao.insertHistoryItem(historyItem)
    fun getAllHistory(): List<HistoryItem> = historyDao.getAllHistory()
    fun deleteHistoryItem(id: Int) = historyDao.deleteHistoryItem(id)
}