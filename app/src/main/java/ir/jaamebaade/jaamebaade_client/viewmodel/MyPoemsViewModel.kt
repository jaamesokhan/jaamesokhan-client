package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatusManager
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyPoemsViewModel @Inject constructor(
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
    private val verseRepository: VerseRepository,
    private val downloadStatusManager: DownloadStatusManager
) : ViewModel() {
    var poets by mutableStateOf<List<Poet>?>(null)
        private set

    var categories by mutableStateOf<List<CategoryGraphNode>?>(null)
        private set

    var randomPoemPreview by mutableStateOf<RandomPoemPreview?>(null)
        private set

    init {
        getAllPoets()
        getAllCategories()
        getRandomPoem()
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

    fun getRandomPoem() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val poemWithPoet = poemRepository.getRandomPoem()
                poemWithPoet?.let {
                    val poemPath = getPoemPath(it.poem.id)
                    val first4Verses = verseRepository.getFirst4VersesByPoemId(it.poem.id)
                    randomPoemPreview = RandomPoemPreview(
                        poemPath = poemPath,
                        verses = first4Verses
                    )
                }
            }
        }
    }

    suspend fun getPoemPath(poemId: Int): VersePoemCategoriesPoet {
        val res = withContext(Dispatchers.IO) {

            val poemWithPoet = fetchPoemWithPoet(poemId)!!
            VersePoemCategoriesPoet(
                verse = null,
                poem = poemWithPoet.poem,
                poet = poemWithPoet.poet,
                categories = fetchAllCategoriesOfPoem(poemWithPoet.poem),
            )
        }
        return res
    }

    private suspend fun fetchAllCategoriesOfPoem(poem: Poem): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryRepository.getAllParentsOfCategoryId(poem.categoryId)
        }
    }

    private suspend fun fetchPoemWithPoet(poemId: Int): PoemWithPoet? {
        return withContext(Dispatchers.IO) {
            try {
                poemRepository.getPoemWithPoet(poemId)
            } catch (_: Exception) {
                null
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
            node.subCategories = createCategoryGraph(categories, node)
            updateSelectedForRandomState(node)
            result.add(node)
        }
        return result
    }

    private fun updateSelectedForRandomState(node: CategoryGraphNode) {
        if (node.subCategories.isNotEmpty()) {
            val allSelected =
                node.subCategories.all { it.selectedForRandomState.value == ToggleableState.On }
            val noneSelected =
                node.subCategories.all { it.selectedForRandomState.value == ToggleableState.Off }
            node.selectedForRandomState.value = when {
                allSelected -> ToggleableState.On
                noneSelected -> ToggleableState.Off
                else -> ToggleableState.Indeterminate
            }
        } else {
            node.selectedForRandomState.value = if (node.category.randomSelected != false) {
                ToggleableState.On
            } else {
                ToggleableState.Off
            }
        }
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
        val randomSelected = category.selectedForRandomState.value != ToggleableState.Off
        category.category.randomSelected = randomSelected
        categoryRepository.updateCategoryRandomSelectedFlag(
            category.category.id,
            randomSelected
        )
        category.subCategories.forEach { saveSelectedCategoriesRecursively(it) }
    }
}
