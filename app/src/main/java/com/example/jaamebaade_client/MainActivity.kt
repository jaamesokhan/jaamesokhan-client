package com.example.jaamebaade_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jaamebaade_client.view.MainScreen
import com.example.jaamebaade_client.viewmodel.PoetViewModel

class MainActivity : ComponentActivity() {
    private val poetViewModel: PoetViewModel by viewModels<PoetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

           MainScreen(poetViewModel = poetViewModel)
        }
    }
}