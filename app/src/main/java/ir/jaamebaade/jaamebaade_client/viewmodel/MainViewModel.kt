package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    init {
        loadText()
    }

    private fun loadText() {
        viewModelScope.launch {
            _text.value = "Hello, MVVM with Jetpack Compose!"
        }
    }
}
