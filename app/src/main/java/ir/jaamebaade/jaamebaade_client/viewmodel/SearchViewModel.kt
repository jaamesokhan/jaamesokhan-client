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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
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


    private val searchHistory: Flow<List<SearchHistoryRecord>> = searchHistoryRepository.getSearchHistory()
    var showingSearchHistory = searchHistory

    init {
        viewModelScope.launch {
            getAllPoets()
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
        searchHistoryRepository.insertSearchHistory(searchHistory)
    }

    private suspend fun getAllPoets() {
        withContext(Dispatchers.IO) {
            _allPoets.value = poetRepository.getAllPoets()
        }

    }

    fun deleteHistoryItem(historyItem: SearchHistoryRecord) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchHistoryRepository.removeHistoryRecord(historyItem)
            }

        }

    }

    fun filterHistoryByQuery() {
        viewModelScope.launch {
            searchHistory.collect { historyRecords ->
                showingSearchHistory = flow {
                    val filteredHistory = historyRecords.filter {
                        it.query.contains(query, ignoreCase = true)
                    }
                    emit(filteredHistory)
                }
            }
        }
    }

}