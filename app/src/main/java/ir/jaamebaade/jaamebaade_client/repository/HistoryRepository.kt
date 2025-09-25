package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.HistoryRecord
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordFirstVerse
import javax.inject.Inject


class HistoryRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val historyDao = db.historyDao()
    fun insertHistoryItem(historyRecord: HistoryRecord) = historyDao.insertHistoryItem(historyRecord)
    fun getAllHistorySortedWithFirstVerse(): List<HistoryRecordFirstVerse> = historyDao.getAllHistorySortedWithFirstVerse()

    fun deleteHistoryItem(id: Int) = historyDao.deleteHistoryItem(id)
    fun clearHistory() = historyDao.clearHistory()
}
