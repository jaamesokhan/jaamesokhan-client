package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.api.JaameSokhanApiClient
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionOptionViewModel @Inject constructor(
    private val jaameSokhanApiClient: JaameSokhanApiClient,
) : ViewModel() {

    private val _apiResult = mutableStateOf("")
    val apiResult: State<String> = _apiResult


    fun getWordMeaning(word: String, successCallBack: () -> Unit, failureCallBack: () -> Unit) {
        viewModelScope.launch {
            val res = jaameSokhanApiClient.getMeaning(word, successCallBack, failureCallBack)
            _apiResult.value = if(res?.isNotEmpty() == true) res else "معنی یافت نشد!"
        }
    }

}