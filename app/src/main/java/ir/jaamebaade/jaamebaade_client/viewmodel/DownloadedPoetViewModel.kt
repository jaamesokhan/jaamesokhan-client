package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatusManager
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadedPoetViewModel @Inject constructor(
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
    private val downloadStatusManager: DownloadStatusManager
) : ViewModel() {
    var poets by mutableStateOf<List<Poet>?>(null)
        private set

    var categories by mutableStateOf<List<CategoryGraphNode>?>(null)
        private set

    init {
        getAllPoets()
        getAllCategories()
    }


    fun saveSelectedCategoriesForRandomPoem() {
        viewModelScope.launch {
            categories?.let {
                saveSelectedCategories(it)
            }
        }
    }

    fun deletePoets(selectedPoets: List<Poet>, onSuccess: () -> Unit) {
        if (selectedPoets.isEmpty()) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                poetRepository.deletePoets(selectedPoets)
                selectedPoets.forEach { changeDownloadStatusToNotDownloaded(it.id.toString()) }
                poets = poets!!.toMutableList().also { it.removeAll(selectedPoets) }
                onSuccess()
            }
        }
    }

    private fun changeDownloadStatusToNotDownloaded(poetId: String) {
        downloadStatusManager.saveDownloadStatus(poetId, DownloadStatus.NotDownloaded)
    }

    private fun createCategoryGraph(
        categories: List<Category>,
        parent: CategoryGraphNode? = null,
    ): List<CategoryGraphNode> {
        val result = mutableListOf<CategoryGraphNode>()
        categories.filter { it.parentId == (parent?.category?.id ?: 0) }.forEach { category ->
            val node = category.toGraphNode(parent = parent)
            val children = categories.filter { it.parentId == category.id }
            val childrenGraph = children.map { it.toGraphNode(parent = node) }
            node.subCategories = childrenGraph
            childrenGraph.forEach { child ->
                child.subCategories = createCategoryGraph(categories, child)
            }
            val allSelected =
                node.subCategories.all { it.selectedForRandomState.value == ToggleableState.On }
            val noneSelected =
                node.subCategories.none { it.selectedForRandomState.value == ToggleableState.On }
            node.selectedForRandomState.value = if (allSelected) {
                ToggleableState.On
            } else if (noneSelected) {
                ToggleableState.Off
            } else {
                ToggleableState.Indeterminate
            }
            result.add(node)
        }
        return result
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            val result = getAllCategoriesFromRepository()
            categories = createCategoryGraph(result)
        }
    }


    private fun getAllPoets() {
        viewModelScope.launch {
            poets = getAllDownloadedPoets()
        }
    }

    suspend fun getPoetCategoryId(poetId: Int): Int {
        return withContext(Dispatchers.IO) {
            categoryRepository.getPoetCategoryId(poetId)
        }
    }

    private suspend fun getAllDownloadedPoets(): List<Poet> {
        val res = withContext(Dispatchers.IO) {
            poetRepository.getAllPoets()
        }
        return res
    }

    private suspend fun getAllCategoriesFromRepository(): List<Category> {
        val res = withContext(Dispatchers.IO) {
            categoryRepository.getAllCategories()
        }
        return res
    }


    private suspend fun saveSelectedCategories(categories: List<CategoryGraphNode>) {
        withContext(Dispatchers.IO) {
            categories.forEach { category ->
                saveSelectedCategoriesRecursively(category)
            }
        }
    }

    private fun saveSelectedCategoriesRecursively(category: CategoryGraphNode) {
        category.category.randomSelected =
            category.selectedForRandomState.value == ToggleableState.On
        categoryRepository.updateCategoryRandomSelectedFlag(
            category.category.id,
            category.selectedForRandomState.value == ToggleableState.On
        )
        category.subCategories.forEach { saveSelectedCategoriesRecursively(it) }
    }
}
