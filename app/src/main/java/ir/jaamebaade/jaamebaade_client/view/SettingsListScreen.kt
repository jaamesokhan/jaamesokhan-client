package ir.jaamebaade.jaamebaade_client.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.utility.UserFontStorage
import ir.jaamebaade.jaamebaade_client.view.components.ConfirmationDialog
import kotlinx.coroutines.launch
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
import ir.jaamebaade.jaamebaade_client.repository.RandomPoemLayoutRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.setting.CustomRadioButton
import ir.jaamebaade.jaamebaade_client.view.components.setting.SettingListItem
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemLayoutPicker
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemOptions
import ir.jaamebaade.jaamebaade_client.view.components.rememberSampleRandomPoemPreview
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListScreen(
    modifier: Modifier = Modifier,
    fontRepository: FontRepository,
    themeRepository: ThemeRepository,
    randomPoemLayoutRepository: RandomPoemLayoutRepository,
    openRandomSettings: Boolean = false,
    openRandomLayout: Boolean = false,
) {
    var selectedPoemFontFamily by remember { mutableStateOf(fontRepository.poemFontFamily.value) }
    val userFonts by fontRepository.userFonts.collectAsState()
    var fontPendingDeletion by remember { mutableStateOf<CustomFont?>(null) }
    var isImportingFont by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fontPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        isImportingFont = true
        coroutineScope.launch {
            val messageRes = when (fontRepository.importUserFont(uri)) {
                is UserFontStorage.ImportResult.Success -> {
                    selectedPoemFontFamily = fontRepository.poemFontFamily.value
                    R.string.USER_FONT_ADDED
                }
                UserFontStorage.ImportResult.TooLarge -> R.string.USER_FONT_TOO_LARGE
                UserFontStorage.ImportResult.InvalidFile -> R.string.USER_FONT_INVALID
            }
            isImportingFont = false
            Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
        }
    }
    var selectedPoemFontSize by remember { mutableStateOf(fontRepository.poemFontSize.value) }
    var selectedTheme by remember { mutableStateOf(themeRepository.appTheme.value) }
    var selectedRandomPoemLayout by remember { mutableStateOf(randomPoemLayoutRepository.layout.value) }
    var selectedSettingItem by remember(openRandomSettings, openRandomLayout) {
        mutableStateOf<SettingItem?>(
            when {
                openRandomSettings -> SettingItem.RANDOM_POEM
                openRandomLayout -> SettingItem.RANDOM_POEM_LAYOUT
                else -> null
            }
        )
    }
    var showBottomSheet by remember(openRandomSettings, openRandomLayout) {
        mutableStateOf(openRandomSettings || openRandomLayout)
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(top = Dimens.space4, bottom = Dimens.space34),
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
                .padding(top = Dimens.space8)
                .padding(horizontal = Dimens.space4)

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
                modifier = Modifier.padding(vertical = Dimens.space4, horizontal = Dimens.space8),
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
                modifier = Modifier.padding(vertical = Dimens.space4, horizontal = Dimens.space8),
                color = MaterialTheme.colorScheme.outline
            )

            SettingListItem(
                "${stringResource(R.string.RANDOM_POEM_LAYOUT)} ${selectedRandomPoemLayout.displayName}",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.poem),
                        contentDescription = stringResource(R.string.RANDOM_POEM_LAYOUT),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }, onClick = {
                    showBottomSheet = true
                    selectedSettingItem = SettingItem.RANDOM_POEM_LAYOUT
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimens.space4, horizontal = Dimens.space8),
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
                modifier = Modifier.padding(vertical = Dimens.space4, horizontal = Dimens.space8),
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
                        val allFonts = CustomFonts.getAllFonts() + userFonts
                        allFonts.forEach { customFont ->
                            fun onFontClick() {
                                selectedPoemFontFamily = customFont
                                fontRepository.setPoemFontFamily(customFont)
                            }
                            CustomRadioButton(
                                title = customFont.displayName,
                                showDivider = true,
                                isSelected = customFont.name == selectedPoemFontFamily.name,
                                trailingContent = if (customFont.isUserFont) {
                                    {
                                        IconButton(onClick = { fontPendingDeletion = customFont }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = stringResource(R.string.DELETE),
                                                tint = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    null
                                },
                            ) { onFontClick() }
                        }
                        AddUserFontItem(
                            isImporting = isImportingFont,
                            onClick = {
                                fontPickerLauncher.launch(FONT_MIME_TYPES)
                            },
                        )
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

                    SettingItem.RANDOM_POEM_LAYOUT -> {
                        RandomPoemLayoutPicker(
                            selected = selectedRandomPoemLayout,
                            samplePoem = rememberSampleRandomPoemPreview(),
                            onSelect = { layout ->
                                selectedRandomPoemLayout = layout
                                randomPoemLayoutRepository.setLayout(layout)
                            },
                        )
                    }

                    null -> {
                        // Do nothing
                    }
                }
            }
        }

    }
    fontPendingDeletion?.let { font ->
        ConfirmationDialog(
            message = stringResource(R.string.USER_FONT_DELETE_CONFIRM, font.displayName),
            onConfirm = {
                fontRepository.deleteUserFont(font)
                selectedPoemFontFamily = fontRepository.poemFontFamily.value
                fontPendingDeletion = null
            },
            onDismiss = { fontPendingDeletion = null },
        )
    }
}

@Composable
private fun AddUserFontItem(
    isImporting: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(enabled = !isImporting, onClick = onClick)
            .padding(horizontal = Dimens.space16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isImporting) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.width(Dimens.space16))
        Text(
            text = stringResource(R.string.USER_FONT_ADD),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

private val FONT_MIME_TYPES = arrayOf(
    "font/ttf",
    "font/otf",
    "font/sfnt",
    "application/x-font-ttf",
    "application/x-font-otf",
    "application/font-sfnt",
    "application/octet-stream",
)


enum class SettingItem {
    FONT, FONT_SIZE, THEME, RANDOM_POEM, RANDOM_POEM_LAYOUT
}
