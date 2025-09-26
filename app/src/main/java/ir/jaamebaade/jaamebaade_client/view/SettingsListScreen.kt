package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.setting.CustomRadioButton
import ir.jaamebaade.jaamebaade_client.view.components.setting.SettingListItem
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListScreen(
    modifier: Modifier = Modifier,
    fontRepository: FontRepository,
    themeRepository: ThemeRepository,
    openRandomSettings: Boolean = false
) {
    var selectedPoemFontFamily by remember { mutableStateOf(fontRepository.poemFontFamily.value) }
    var selectedPoemFontSize by remember { mutableStateOf(fontRepository.poemFontSize.value) }
    var selectedTheme by remember { mutableStateOf(themeRepository.appTheme.value) }
    var selectedSettingItem by remember(openRandomSettings) {
        mutableStateOf<SettingItem?>(if (openRandomSettings) SettingItem.RANDOM_POEM else null)
    }
    var showBottomSheet by remember(openRandomSettings) { mutableStateOf(openRandomSettings) }
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
                val myTextStyle = TextStyle(
                    fontFamily = selectedPoemFontFamily.fontFamily,
                    fontSize = fontRepository.getPoemFontNumberFromSize(selectedPoemFontSize),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_ONE_1))
                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_ONE_2))
                Spacer(modifier = Modifier.size(8.dp))
                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_TWO_1))
                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_TWO_2))
                Spacer(modifier = Modifier.size(8.dp))

                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_THREE_1))
                Text(style = myTextStyle, text = stringResource(R.string.SETTING_VERSE_THREE_2))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp)

        ) {
            SettingListItem(
                "${stringResource(R.string.THEME_SETTING_STR)} ${selectedTheme.displayName}",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.theme),
                        contentDescription = stringResource(R.string.APP_THEME),
                        tint = MaterialTheme.colorScheme.onBackground,
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
                stringResource(R.string.RANDOM_POEM_OPTIONS),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM_OPTIONS),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }, onClick = {
                    showBottomSheet = true
                    selectedSettingItem = SettingItem.RANDOM_POEM
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 9.dp),
                color = MaterialTheme.colorScheme.outline
            )

            SettingListItem(
                "${stringResource(R.string.FONT)} ${selectedPoemFontFamily.displayName}",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.font),
                        contentDescription = stringResource(R.string.FONT),
                        tint = MaterialTheme.colorScheme.onBackground,
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
                "${stringResource(R.string.FONT_SIZE)} ${selectedPoemFontSize.displayName}",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.font_size),
                        contentDescription = stringResource(R.string.FONT_SIZE),
                        tint = MaterialTheme.colorScheme.onBackground,
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
        CustomBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
        ) {
            val sheetModifier = if (selectedSettingItem == SettingItem.RANDOM_POEM) {
                Modifier
            } else {
                Modifier.selectableGroup()
            }
            Column(modifier = sheetModifier) {
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
                                selectedPoemFontSize = fontSize
                                fontRepository.setPoemFontSize(fontSize)
                            }
                            CustomRadioButton(
                                title = fontRepository.getFontNameFromSize(fontSize),
                                showDivider = index != fontRepository.getAvailableFontSizes().lastIndex,
                                isSelected = fontSize == selectedPoemFontSize,
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

                    SettingItem.RANDOM_POEM -> {
                        RandomPoemOptions()
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
    FONT, FONT_SIZE, THEME, RANDOM_POEM
}
