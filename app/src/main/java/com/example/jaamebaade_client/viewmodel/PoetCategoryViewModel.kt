package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.repository.CategoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = PoetCategoryViewModel.PoetCategoryViewModelFactory::class)
class PoetCategoryViewModel @AssistedInject constructor(
    @Assisted("poetId") private val poetId: Int,
    @Assisted("parentIds") private val parentIds: IntArray,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        fetchPoetCategoriesWithParentId(poetId = poetId, parentIds = parentIds)
    }

    @AssistedFactory
    interface PoetCategoryViewModelFactory {
        fun create(
            @Assisted("poetId") poetId: Int,
            @Assisted("parentIds") parentIds: IntArray
        ): PoetCategoryViewModel
    }

    private fun fetchPoetCategoriesWithParentId(poetId: Int, parentIds: IntArray) {
        viewModelScope.launch {
            categories = getPoetCategoriesFromRepository(poetId, parentIds.last())
        }
    }

    suspend fun getPoetCategoriesFromRepository(
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