package com.example.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.repository.ThemeRepository
import com.example.jaamebaade_client.ui.theme.AppThemeType

@Composable
fun ChangeThemeScreen(
    modifier: Modifier = Modifier,
    themeRepository: ThemeRepository,
) {
    val appTheme by themeRepository.appTheme.collectAsState()
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AppThemeType.entries.forEachIndexed { index, item ->
            val isSelected = appTheme == item
            val rowModifier = if (isSelected) {
                Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
            } else {
                Modifier
            }
            Row(
                modifier = rowModifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { themeRepository.setAppThemePreference(item) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.displayName, style = MaterialTheme.typography.bodyLarge)
                if (isSelected) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
            if (index != AppThemeType.entries.size - 1) {
                HorizontalDivider()
            }
        }
    }
}