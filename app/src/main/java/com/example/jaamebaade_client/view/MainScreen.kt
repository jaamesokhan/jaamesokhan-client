package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.view.components.Navbar
import com.example.jaamebaade_client.view.components.TopBar

@Composable
fun MainScreen() {
    JaamebaadeclientTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Column (modifier = Modifier.fillMaxSize()){
                TopBar(innerPadding)
                Spacer(modifier = Modifier.weight(1f)) // TODO this should be replaced by an actual component
                Navbar()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
