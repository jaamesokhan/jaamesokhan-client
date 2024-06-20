package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.repository.PoemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel(assistedFactory = PoemsListViewModel.PoemsListViewModelFactory::class)
class PoemsListViewModel @AssistedInject constructor(
    @Assisted("categoryId") private val categoryId: Int,
    private val poemRepository: PoemRepository,
) : ViewModel() {
    var poems by mutableStateOf<List<Poem>>(emptyList())
        private set
    var isLoading by mutableStateOf<Boolean>(true)
    @AssistedFactory
    interface PoemsListViewModelFactory {
        fun create(
            @Assisted("categoryId") categoryId: Int,
        ): PoemsListViewModel
    }

    init {
        isLoading = true
//        getAllPoems()
    }

    private suspend fun getPoemsFromDatabase(){
        withContext(Dispatchers.IO) {
            poems = poemRepository.getPoemsByCategory(categoryId)
        }

    }
    private fun getAllPoems() {
        viewModelScope.launch {
            getPoemsFromDatabase()
            isLoading = false

        }
    }
    fun getPoemsByCategoryId(categoryId: Int): Flow<PagingData<Poem>> {
        return poemRepository.getPoemsByCategoryIdPaged(categoryId)
            .cachedIn(viewModelScope)
    }


}



