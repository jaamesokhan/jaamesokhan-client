package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.VisitHistoryViewItem
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.HistoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PoemHistoryViewModel @Inject constructor(
    private val poemRepository: PoemRepository,
    private val categoryRepository: CategoryRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {


    // Map of timestamps to PoemPoetCategory objects
    var poemHistory by mutableStateOf<List<VisitHistoryViewItem>>(emptyList())
        private set

    init {
        loadPoemHistory()
    }

    // Fetch PoemWithPoet from the repository
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

    private fun loadPoemHistory() {
        viewModelScope.launch {
            val historyItems = withContext(Dispatchers.IO) {
                historyRepository.getAllHistory() // Adjust this method based on your repository
            }

            val historyList = mutableListOf<VisitHistoryViewItem>()

            for (historyItem in historyItems) {
                val timestamp = historyItem.timestamp // Assuming `timestamp` is a Long field in the history item
                val poemId = historyItem.poemId

                val poemWithPoet = fetchPoemWithPoet(poemId)
                val categories = fetchAllCategories(poemWithPoet!!.poem)
                val poemPoetCategory = VersePoemCategoriesPoet(
                    verse = null,
                    poem = poemWithPoet.poem,
                    poet = poemWithPoet.poet,
                    categories = categories
                )

                historyList.add(VisitHistoryViewItem(historyItem.id, timestamp, poemPoetCategory))
            }


            poemHistory = historyList.sortedByDescending { it.timestamp }
        }
    }

    fun onDeleteItemFromHistoryClicked(id: Int) {
        viewModelScope.launch {
            poemHistory = poemHistory.toMutableList().filterNot { it.id == id }
            deleteItemFromHistory(id)
          }
    }
    private suspend fun deleteItemFromHistory(id: Int){
        withContext(Dispatchers.IO){
            historyRepository.deleteHistoryItem(id)
        }
    }

}
