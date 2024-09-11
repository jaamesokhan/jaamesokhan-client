package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import kotlinx.coroutines.flow.Flow
import java.lang.Appendable
import javax.inject.Inject


class SearchHistoryRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    private val db = appDatabase
    private val searchHistoryDao = db.searchHistoryDao()

    suspend fun insertSearchHistory(searchHistoryRecord: SearchHistoryRecord) {
        searchHistoryDao.insertSearchHistory(searchHistoryRecord)
    }

    fun getSearchHistory(): Flow<List<SearchHistoryRecord>> {
        return searchHistoryDao.getSearchHistory()
    }

    fun removeHistoryRecord(historyRecord: SearchHistoryRecord) {
        searchHistoryDao.removeHistoryRecord(historyRecord.id)
    }
}
