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
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
    val poemsPageData: Flow<PagingData<Poem>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            poemRepository.getPoemPagingSource(
                categoryId
            )
        }
    ).flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface PoemsListViewModelFactory {
        fun create(
            @Assisted("categoryId") categoryId: Int,
        ): PoemsListViewModel
    }


}



