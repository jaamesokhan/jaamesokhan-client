package com.example.jaamebaade_client.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.repository.PoetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadedPoetViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val poetRepository: PoetRepository,

    ) : ViewModel() {
    var poets by mutableStateOf<List<Poet>>(emptyList())
        private set

    init {
        getAllPoets()
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
