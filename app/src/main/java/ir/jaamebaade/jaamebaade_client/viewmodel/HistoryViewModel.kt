package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordPathFirstVerse
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.HistoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val poemRepository: PoemRepository,
    private val categoryRepository: CategoryRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _poemHistory = MutableStateFlow<List<HistoryRecordPathFirstVerse>>(emptyList())
    val poemHistory:StateFlow< List<HistoryRecordPathFirstVerse>> = _poemHistory

    init {
        loadPoemHistory()
    }

    private suspend fun fetchPoemWithPoet(poemId: Int): PoemWithPoet? {
        return withContext(Dispatchers.IO) {
            try {
                poemRepository.getPoemWithPoet(poemId)
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun fetchAllCategories(poem: Poem): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryRepository.getAllParentsOfCategoryId(poem.categoryId)
        }
    }

    fun loadPoemHistory() {
        viewModelScope.launch {
            val historyItems = withContext(Dispatchers.IO) {
                historyRepository.getAllHistorySorted()
            }

            val historyList = mutableListOf<HistoryRecordPathFirstVerse>()

            for (historyItem in historyItems) {
                val timestamp =
                    historyItem.timestamp
                val poemId = historyItem.poemId

                val poemWithPoet = fetchPoemWithPoet(poemId)
                val categories = fetchAllCategories(poemWithPoet!!.poem)
                val poemPoetCategory = VersePoemCategoriesPoet(
                    verse = null,
                    poem = poemWithPoet.poem,
                    poet = poemWithPoet.poet,
                    categories = categories
                )
                val firstVerse = withContext(Dispatchers.IO) {
                    poemRepository.getPoemFirstVerse(poemId)
                }

                historyList.add(HistoryRecordPathFirstVerse(historyItem.id, timestamp, poemPoetCategory, firstVerse))
            }


            _poemHistory.value = historyList.sortedByDescending { it.timestamp }
        }
    }

    fun deleteHistoryRecord(id: Int) {
        viewModelScope.launch {
            deleteHistoryRecordFromRepository(id)
            _poemHistory.value = _poemHistory.value.toMutableList().filterNot { it.id == id }
        }
    }

    private suspend fun deleteHistoryRecordFromRepository(id: Int) {
        withContext(Dispatchers.IO) {
            historyRepository.deleteHistoryItem(id)
        }
    }

}
