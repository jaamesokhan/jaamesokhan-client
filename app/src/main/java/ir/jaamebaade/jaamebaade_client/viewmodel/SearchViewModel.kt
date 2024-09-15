package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.SearchHistoryRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val verseRepository: VerseRepository,
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    var query by mutableStateOf("")

    var poetFilter by mutableStateOf<Poet?>(null)
    var results by mutableStateOf<List<VersePoemCategoriesPoet>>(emptyList())
        private set

    private val _allPoets = MutableStateFlow<List<Poet>>(emptyList())
    val allPoets = _allPoets.asStateFlow()


    private val searchHistoryList: Flow<List<SearchHistoryRecord>> =
        searchHistoryRepository.getSearchHistoryRecords()
    private val _showingSearchHistory = MutableStateFlow<List<SearchHistoryRecord>>(emptyList())
    val showingSearchHistory = _showingSearchHistory.asStateFlow()

    private var searchJob: Job? = null
    init {
        viewModelScope.launch {
            getAllPoets()
            collectSearchHistory()
        }
    }


    fun search(callBack: () -> Unit) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                callBack()
                return@launch
            }
            saveSearchHistory(query)
            runSearchOnDatabase(callBack)
        }
    }

    fun clearSearch() {
        query = ""
        results = emptyList()
        poetFilter = null
        viewModelScope.launch {
            collectSearchHistory()
        }
    }

    fun deleteHistoryItem(historyItem: SearchHistoryRecord) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchHistoryRepository.removeSearchHistoryRecord(historyItem)
            }

        }

    }

    fun onQueryChanged() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            filterHistoryByQuery(query)
        }
    }

    private suspend fun collectSearchHistory() {
        searchHistoryList.collectLatest { historyRecords ->
            _showingSearchHistory.value = historyRecords
        }
    }

    private suspend fun runSearchOnDatabase(callBack: () -> Unit) {
        withContext(Dispatchers.IO) {
            val tempResults = verseRepository.searchVerses(query, poetFilter?.id)

            results = tempResults.map {
                val parentCategories = categoryRepository.getAllParentsOfCategoryId(it.category.id)
                VersePoemCategoriesPoet(
                    verse = it.verse,
                    poem = it.poem,
                    poet = it.poet,
                    categories = parentCategories,
                )

            }

            callBack()
        }
    }

    private suspend fun saveSearchHistory(searchQuery: String) {
        val searchHistory = SearchHistoryRecord(
            query = searchQuery,
            timestamp = System.currentTimeMillis()
        )
        searchHistoryRepository.insertSearchHistoryRecord(searchHistory)
    }

    private suspend fun getAllPoets() {
        withContext(Dispatchers.IO) {
            _allPoets.value = poetRepository.getAllPoets()
        }

    }


    private suspend fun filterHistoryByQuery(query: String) {
        searchHistoryList.collectLatest { historyRecords ->
            val filteredHistory = historyRecords.filter {
                it.query.contains(query, ignoreCase = true)
            }
            _showingSearchHistory.value = filteredHistory
        }
    }

}