package com.example.jaamebaade_client.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.repository.PoetRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaamebaade_client.model.Poet
import android.util.Log

class PoetViewModel : ViewModel() {
    private val repository = PoetRepository()

    var poets by mutableStateOf<List<Poet>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchPoets()
    }

    private fun fetchPoets() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = repository.getPoets()
                for (poet in response) {
                    Log.d("poets", "${poet.name}, + ${poet.imageURL}")
                }
                isLoading = false
                if (response.isNotEmpty()) {
                    poets = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
//        poets = listOf(
//            Poet("حافظ", R.drawable.hafez) ,
//            Poet("خیام", R.drawable.hafez),
//            Poet("فردوسی", R.drawable.hafez),
//            Poet("فردوس", R.drawable.hafez),
//            Poet("فردوسی1", R.drawable.hafez),
//            Poet("فردوسی2", R.drawable.hafez),
//            Poet("فردوسی3", R.drawable.hafez),
//            Poet("فردوسی4", R.drawable.hafez),
//            Poet("فردوسی5", R.drawable.hafez),
//            Poet("فردوسی6", R.drawable.hafez),
//            Poet("فردوسی7", R.drawable.hafez),
//            Poet("فردوسی8", R.drawable.hafez),
//            Poet("فردوسی9", R.drawable.hafez),
//            Poet("فردوسی10", R.drawable.hafez),
//            Poet("فردوسی11", R.drawable.hafez),
//            Poet("فردوسی12", R.drawable.hafez),
//            Poet("فردوسی13", R.drawable.hafez),
//            Poet("فردوسی14", R.drawable.hafez),
//            Poet("فردوسی15", R.drawable.hafez),
//            Poet("فردوسی16", R.drawable.hafez),
//
//            )
    }
}