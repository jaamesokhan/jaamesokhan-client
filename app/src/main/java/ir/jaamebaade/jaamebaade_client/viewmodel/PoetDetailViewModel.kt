package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.CategoryWithPoemCount
import ir.jaamebaade.jaamebaade_client.model.PoemWithFirstVerse
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = PoetDetailViewModel.PoetDetailViewModelFactory::class)
class PoetDetailViewModel @AssistedInject constructor(
    @Assisted("poetId") private val poetId: Int,
    @Assisted("parentIds") private val parentIds: IntArray,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
) : ViewModel() {

    var categories by mutableStateOf<List<CategoryWithPoemCount>>(emptyList())
        private set

    val poemsPageData: Flow<PagingData<PoemWithFirstVerse>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            poemRepository.getPoemPagingSource(
                parentIds.last()
            )
        }
    ).flow.cachedIn(viewModelScope)

    init {
        fetchPoetCategoriesWithParentId(poetId = poetId, parentIds = parentIds)
    }

    @AssistedFactory
    interface PoetDetailViewModelFactory {
        fun create(
            @Assisted("poetId") poetId: Int,
            @Assisted("parentIds") parentIds: IntArray
        ): PoetDetailViewModel
    }


    suspend fun findShuffledPoem(): PoemWithPoet? {
        return withContext(Dispatchers.IO) {
            var randomPoem: PoemWithPoet? = null
            randomPoem = poemRepository.getRandomPoem(parentIds.last())
            return@withContext randomPoem
        }
    }

    private fun fetchPoetCategoriesWithParentId(poetId: Int, parentIds: IntArray) {
        viewModelScope.launch {
            categories = getPoetCategoriesFromRepository(poetId, parentIds.last())
        }
    }

    private suspend fun getPoetCategoriesFromRepository(
        poetId: Int,
        parentId: Int
    ): List<CategoryWithPoemCount> {
        val res = withContext(Dispatchers.IO) {
            categoryRepository.getCategoriesByPoetIdFilteredByParentIdWithPoemCount(
                poetId,
                parentId
            )
        }
        return res
    }
}