package ir.jaamebaade.jaamebaade_client.view

import android.os.Build
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType

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
        Text("پوسته‌ها",style = MaterialTheme.typography.titleMedium ,fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp,2.dp))
        AppThemeType.entries.forEachIndexed { index, item ->
            val currentapiVersion = Build.VERSION.SDK_INT

            if (currentapiVersion < 11 && item == AppThemeType.SYSTEM_AUTO) {
                return@forEachIndexed
            }
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.displayName, style = MaterialTheme.typography.bodyMedium)
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
        Text("رنگ‌ها",style = MaterialTheme.typography.titleMedium ,fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp,1.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val appColor by themeRepository.appColor.collectAsState()
            var checked by remember { mutableStateOf(appColor) }
            Column { Text("رنگ‌های پویا", style = MaterialTheme.typography.bodyMedium)
                Text("برای اندروید ۱۳ یا بالاتر", style = MaterialTheme.typography.labelSmall)}
            Switch(
                checked = checked,
                onCheckedChange = {
                    themeRepository.setAppColorPreference(it)
                    checked = it
                }
            )
        }
    }
}