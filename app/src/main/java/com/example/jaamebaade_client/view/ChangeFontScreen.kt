package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.view.components.FontFamilyMenu
import com.example.jaamebaade_client.view.components.FontSizeTileMenu

@Composable
fun ChangeFontScreen(modifier: Modifier, fontRepository: FontRepository) {

    Column(modifier = modifier.padding(8.dp)) {
        Text("اندازه فونت")
        FontSizeTileMenu(fontRepository)

        Text("فونت برنامه")
        FontFamilyMenu(fontRepository = fontRepository)

    }
}

