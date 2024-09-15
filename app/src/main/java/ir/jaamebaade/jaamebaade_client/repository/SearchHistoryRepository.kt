package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchHistoryRepository @Inject constructor(
    appDatabase: AppDatabase
) {
    private val db = appDatabase
    private val searchHistoryDao = db.searchHistoryDao()

    suspend fun insertSearchHistoryRecord(searchHistoryRecord: SearchHistoryRecord) {
        searchHistoryDao.insertSearchHistoryRecord(searchHistoryRecord)
    }

    fun getSearchHistoryRecords(): Flow<List<SearchHistoryRecord>> {
        return searchHistoryDao.getSearchHistoryRecords()
    }

    fun removeSearchHistoryRecord(historyRecord: SearchHistoryRecord) {
        searchHistoryDao.removeSearchHistoryRecord(historyRecord.id)
    }
}
