package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
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
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.view.components.setting.CustomRadioButton
import ir.jaamebaade.jaamebaade_client.view.components.setting.SettingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListScreen(modifier: Modifier = Modifier, fontRepository: FontRepository) {
    //val selectedPoemFontFamily by val fontRepository.poemFontFamily.
    var selectedPoemFontFamily by remember { mutableStateOf(fontRepository.poemFontFamily.value) }
    var selectPoemFontSize by remember { mutableStateOf(fontRepository.poemFontSizeIndex.value) }
    var selectedSettingItem by remember { mutableStateOf<SettingItem?>(null) }
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
                }, onClick = {
                    showBottomSheet = true
                    selectedSettingItem = SettingItem.THEME
                }
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
                }, onClick = {
                    showBottomSheet = true
                    selectedSettingItem = SettingItem.FONT
                }
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
                }, onClick = {
                    showBottomSheet = true
                    selectedSettingItem = SettingItem.FONT_SIZE
                }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .selectableGroup()
            ) {
                when (selectedSettingItem) {
                    SettingItem.FONT -> {
                        CustomFonts.getAllFonts().forEachIndexed { index, customFont ->
                            fun onFontClick() {
                                selectedPoemFontFamily = customFont
                                fontRepository.setPoemFontFamily(customFont)
                                showBottomSheet = false
                            }
                            CustomRadioButton(
                                title = customFont.displayName,
                                showDivider = index != CustomFonts.getAllFonts().lastIndex,
                                isSelected = customFont == selectedPoemFontFamily,
                            ) { onFontClick() }

                        }

                    }


                    SettingItem.FONT_SIZE -> {
                        // Font size selection logic here
                    }

                    SettingItem.THEME -> {
                        // Theme selection logic here
                    }

                    null -> {
                        // Do nothing
                    }
                }
            }
        }

    }
}


enum class SettingItem {
    FONT, FONT_SIZE, THEME
}