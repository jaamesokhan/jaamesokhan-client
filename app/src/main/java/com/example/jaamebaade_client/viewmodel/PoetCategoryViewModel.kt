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
    @Assisted("parentId") private val parentId: Int,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        fetchPoetCategoriesWithParentId(poetId = poetId, parentId = parentId)
    }

    @AssistedFactory
    interface PoetCategoryViewModelFactory {
        fun create(
            @Assisted("poetId") poetId: Int,
            @Assisted("parentId") parentId: Int
        ): PoetCategoryViewModel
    }

    private fun fetchPoetCategoriesWithParentId(poetId: Int, parentId: Int) {
        viewModelScope.launch {
            categories = getPoetCategoriesFromRepository(poetId, parentId)
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