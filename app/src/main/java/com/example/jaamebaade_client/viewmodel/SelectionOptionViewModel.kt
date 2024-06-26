package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.api.DictionaryApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionOptionViewModel @Inject constructor(
    private val dictionaryApiClient: DictionaryApiClient,
) : ViewModel() {

    private val _apiResult = mutableStateOf("")
    val apiResult: State<String> = _apiResult


    fun getWordMeaning(word: String) {
        viewModelScope.launch {
            val res = dictionaryApiClient.getMeaning(word)
            _apiResult.value = res?: "معنی یافت نشد!"
        }
    }

}