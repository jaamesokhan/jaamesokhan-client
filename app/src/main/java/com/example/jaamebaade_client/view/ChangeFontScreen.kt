package com.example.jaamebaade_client.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.FONT_SCALE
import com.example.jaamebaade_client.ui.theme.getFontByFontFamilyName
import com.example.jaamebaade_client.utility.restartApp
import com.example.jaamebaade_client.view.components.ConfirmationDialog
import com.example.jaamebaade_client.view.components.FontFamilyMenu
import com.example.jaamebaade_client.view.components.FontSizeTileMenu

@Composable
fun ChangeFontScreen(modifier: Modifier, fontRepository: FontRepository) {

    Column(modifier = modifier) {
        Text("اندازه فونت")
        FontSizeTileMenu(fontRepository)

        Text("فونت برنامه")
        FontFamilyMenu(fontRepository = fontRepository)

    }
}

