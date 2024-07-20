package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.repository.CategoryRepository
import com.example.jaamebaade_client.repository.PoetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadedPoetViewModel @Inject constructor(
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    var poets by mutableStateOf<List<Poet>?>(null)
        private set


    init {
        getAllPoets()
    }

    suspend fun getPoetCategoryId(poetId: Int): Int {
        return withContext(Dispatchers.IO) {
            categoryRepository.getPoetCategoryId(poetId)
        }
    }

    private fun getAllPoets() {
        viewModelScope.launch {
            poets = getAllDownloadedPoets()
        }
    }

    private suspend fun getAllDownloadedPoets(): List<Poet> {
        val res = withContext(Dispatchers.IO) {
            poetRepository.getAllPoets()
        }
        return res
    }
}
