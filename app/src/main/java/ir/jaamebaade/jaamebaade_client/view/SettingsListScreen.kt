package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.view.components.setting.SettingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListScreen(modifier: Modifier = Modifier, fontRepository: FontRepository) {
    var selectedPoemFontFamily by remember { mutableStateOf(fontRepository.poemFontFamily.value) }
    var selectPoemFontSize by remember { mutableStateOf(fontRepository.poemFontSizeIndex.value) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت اول")
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت اول")
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت دوم")
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت دوم")
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت سوم")
                Text(fontFamily = selectedPoemFontFamily.fontFamily, text = "بیت سوم")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp)
        ) {
            SettingListItem(
                "حالت روز",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }, onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 9.dp),
                color = MaterialTheme.colorScheme.outline
            )

            SettingListItem(
                "فونت ${selectedPoemFontFamily.displayName}",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }, onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 9.dp),
                color = MaterialTheme.colorScheme.outline
            )
            SettingListItem(
                "اندازه قلم $selectPoemFontSize",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }, onClick = {}
            )

        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
        }

    }
}




