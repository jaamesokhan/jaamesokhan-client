package com.example.jaamebaade_client.view


import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.view.components.LoadingIndicator
import com.example.jaamebaade_client.view.components.PoetItem
import com.example.jaamebaade_client.viewmodel.PoetViewModel
import androidx.compose.ui.Modifier
@Composable
fun PoetsScreen(poetViewModel: PoetViewModel, modifier: Modifier = Modifier) {
    val poets = poetViewModel.poets

    val isLoading = poetViewModel.isLoading
    Box(modifier = modifier) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            PoetsList(poets) { }
        }
    }
}

@Composable
fun PoetsList(poets: List<Poet>, onPoetClick: (Poet) -> Unit) {
    val context = LocalContext.current
    LazyColumn(userScrollEnabled = true){
        items(poets) { poet ->
            PoetItem(poet = poet) {
                onPoetClick(poet)
                Toast.makeText(context, "${poet.name} clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
