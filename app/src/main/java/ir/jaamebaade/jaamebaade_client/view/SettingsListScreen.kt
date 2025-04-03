package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN20
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.view.components.setting.CustomRadioButton
import ir.jaamebaade.jaamebaade_client.view.components.setting.SettingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListScreen(
    modifier: Modifier = Modifier,
    fontRepository: FontRepository,
    themeRepository: ThemeRepository
) {
    var selectedPoemFontFamily by remember { mutableStateOf(fontRepository.poemFontFamily.value) }
    var selectPoemFontSizeIndex by remember { mutableStateOf(fontRepository.poemFontSize.value) }
    var selectedTheme by remember { mutableStateOf(themeRepository.appTheme.value) }
    var selectedSettingItem by remember { mutableStateOf<SettingItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(top = 5.dp, bottom = 32.dp),
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
                        painter = painterResource(id = R.drawable.theme),
                        contentDescription = "تم برنامه",
                        tint = MaterialTheme.colorScheme.neutralN20,
                        modifier = Modifier.size(32.dp)
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
                        painter = painterResource(id = R.drawable.font),
                        contentDescription = "فونت",
                        tint = MaterialTheme.colorScheme.neutralN20,
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer(scaleX = -1f)
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
                "اندازه قلم $selectPoemFontSizeIndex",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.font_size),
                        contentDescription = "اندازه قلم",
                        tint = MaterialTheme.colorScheme.neutralN20,
                        modifier = Modifier.size(32.dp)
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
            containerColor = MaterialTheme.colorScheme.neutralN95,
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
            ) {
                when (selectedSettingItem) {
                    SettingItem.FONT -> {
                        CustomFonts.getAllFonts().forEachIndexed { index, customFont ->
                            fun onFontClick() {
                                selectedPoemFontFamily = customFont
                                fontRepository.setPoemFontFamily(customFont)
                            }
                            CustomRadioButton(
                                title = customFont.displayName,
                                showDivider = index != CustomFonts.getAllFonts().lastIndex,
                                isSelected = customFont == selectedPoemFontFamily,
                            ) { onFontClick() }

                        }

                    }


                    SettingItem.FONT_SIZE -> {
                        fontRepository.getAvailableFontSizes().forEachIndexed { index, fontSize ->
                            fun onFontSizeClick() {
                                selectPoemFontSizeIndex = fontSize
                                fontRepository.setPoemFontSize(fontSize)
                            }
                            CustomRadioButton(
                                title = fontRepository.getFontNameFromSize(fontSize),
                                showDivider = index != CustomFonts.getAllFonts().lastIndex,
                                isSelected = fontSize == selectPoemFontSizeIndex,
                            ) { onFontSizeClick() }
                        }
                    }

                    SettingItem.THEME -> {
                        AppThemeType.entries.forEachIndexed { index, appThemeType ->
                            fun onThemeClick() {
                                selectedTheme = appThemeType
                                themeRepository.setAppThemePreference(appThemeType)
                            }
                            CustomRadioButton(
                                title = appThemeType.displayName,
                                showDivider = index != AppThemeType.entries.lastIndex,
                                isSelected = appThemeType == selectedTheme,
                            ) { onThemeClick() }
                        }
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