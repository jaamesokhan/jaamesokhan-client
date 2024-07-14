package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.repository.CategoryRepository
import com.example.jaamebaade_client.repository.PoemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = PoetCategoryPoemViewModel.PoetCategoryPoemViewModelFactory::class)
class PoetCategoryPoemViewModel @AssistedInject constructor(
    @Assisted("poetId") private val poetId: Int,
    @Assisted("parentIds") private val parentIds: IntArray,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
) : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    val poemsPageData: Flow<PagingData<Poem>> = Pager(
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
    interface PoetCategoryPoemViewModelFactory {
        fun create(
            @Assisted("poetId") poetId: Int,
            @Assisted("parentIds") parentIds: IntArray
        ): PoetCategoryPoemViewModel
    }

    private fun fetchPoetCategoriesWithParentId(poetId: Int, parentIds: IntArray) {
        viewModelScope.launch {
            categories = getPoetCategoriesFromRepository(poetId, parentIds.last())
        }
    }

    private suspend fun getPoetCategoriesFromRepository(
        poetId: Int,
        parentId: Int
    ): List<Category> {
        val res = withContext(Dispatchers.IO) {
            categoryRepository.getCategoriesByPoetIdFilteredByParentId(
                poetId,
                parentId
            )
        }
        return res
    }
}