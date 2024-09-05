package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord
import javax.inject.Inject


class HistoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val historyDao = db.historyDao()
    fun insertHistoryItem(historyRecord: HistoryRecord) = historyDao.insertHistoryItem(historyRecord)
    fun getAllHistorySorted(): List<HistoryRecord> = historyDao.getAllHistorySorted()
    fun deleteHistoryItem(id: Int) = historyDao.deleteHistoryItem(id)
}