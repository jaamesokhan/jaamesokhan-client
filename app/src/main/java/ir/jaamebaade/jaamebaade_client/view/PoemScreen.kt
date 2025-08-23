package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.view.components.AudioListItems
import ir.jaamebaade.jaamebaade_client.view.components.NotesBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.VerseItem
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemMoreOptionsList
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemOptionItem
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenActionHeader
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenBottomToolBar
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenTitle
import ir.jaamebaade.jaamebaade_client.view.components.poem.ToggleButtonItem
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemScreen(
    navController: NavController,
    poemId: Int,
    poetId: Int,
    focusedVerseId: Long?,
    modifier: Modifier,
    audioViewModel: AudioViewModel = hiltViewModel(),
    fontRepository: FontRepository
) {
    val context = LocalContext.current
    val verseStyle = SpanStyle(
        fontFamily = fontRepository.getPoemFontFamily(),
        fontSize = fontRepository.getPoemFontSize()
    )
    var path by remember(poemId) {
        mutableStateOf<VersePoemCategoriesPoet?>(null)
    }

    var poemTitle by remember(poemId) {
        mutableStateOf("")
    }

    val poemViewModel =
        hiltViewModel<PoemViewModel, PoemViewModel.VerseViewModelFactory> { factory ->
            factory.create(
                poemId, poetId
            )
        }

    LaunchedEffect(poemId) {
        poemViewModel.onPoemVisited(poemId)
    }
    var minId by remember { mutableIntStateOf(0) }
    var maxId by remember { mutableIntStateOf(0) }

    var shouldFocusForSearch by remember { mutableStateOf(false) }
    var shouldFocusForRecitation by remember { mutableStateOf(false) }


    var showNotes by remember { mutableStateOf(false) }

    var showHighlights by remember { mutableStateOf(true) }
    var showVerseNumbers by remember { mutableStateOf(false) }
    var selectMode by remember { mutableStateOf(false) }
    val isBookmarked by poemViewModel.isBookmarked.collectAsState()

    val lazyListState = rememberLazyListState()

    val mediaPlayer = audioViewModel.mediaPlayer
    val playStatus = audioViewModel.playStatus
    val syncInfoFetchStatus = poemViewModel.syncInfoFetchStatus
    val audioSyncData = poemViewModel.audioSyncInfo.collectAsState().value
    var recitedVerseIndex by remember { mutableIntStateOf(0) }

    val versesWithHighlights by poemViewModel.verses.collectAsState()

    val focusedVerse = versesWithHighlights.find { it.verse.id == focusedVerseId }

    val selectedVerses = remember { mutableStateListOf<VerseWithHighlights>() }


    fun onClick(boolean: Boolean, item: VerseWithHighlights) {
        if (boolean) selectedVerses.remove(item)
        else selectedVerses.add(item)

        if (selectedVerses.isEmpty()) selectMode = false
    }

    LaunchedEffect(poetId) {
        path = poemViewModel.getPoemPath(poemId)
        val categoryId = poemViewModel.getCategoryIdByPoemId(poemId)
        val minMaxPair = poemViewModel.getFirstAndLastWithCategoryId(categoryId)
        minId = minMaxPair.first
        maxId = minMaxPair.second
    }

    LaunchedEffect(poemId) {
        poemTitle = poemViewModel.getPoemTitle(poemId)
    }

    if (focusedVerse != null) {
        LaunchedEffect(key1 = focusedVerse) {
            lazyListState.animateScrollToItem(index = versesWithHighlights.indexOf(focusedVerse))
            shouldFocusForSearch = true
            delay(2000)
            shouldFocusForSearch = false
        }
    }

    LaunchedEffect(playStatus) {
        if (playStatus == Status.FINISHED || playStatus == Status.NOT_STARTED) {
            shouldFocusForRecitation = false
        } else if (syncInfoFetchStatus == Status.SUCCESS && audioSyncData != null) {
            while (mediaPlayer.isPlaying) {
                val currentPosition =
                    mediaPlayer.currentPosition + (audioSyncData.poemAudio?.oneSecondBugFix ?: 0)
                val syncInfo = audioSyncData.poemAudio?.syncArray?.syncInfo?.findLast {
                    currentPosition >= it.audioMiliseconds!!
                }

                if (syncInfo?.verseOrder != null && syncInfo.verseOrder!! >= 0) {
                    recitedVerseIndex = syncInfo.verseOrder!!
                    shouldFocusForRecitation = true
                } else {
                    shouldFocusForRecitation = false
                }
                delay(50)
            }
        }
    }

    LaunchedEffect(recitedVerseIndex) {
        lazyListState.animateScrollToItem(index = recitedVerseIndex)
    }

    var audioOptionChecked by remember { mutableStateOf(false) }
    var moreOptionsChecked by remember { mutableStateOf(false) }


    val toggleButtonItems = listOf(
        ToggleButtonItem(
            checkedImageVector = Icons.AutoMirrored.Filled.VolumeUp,
            uncheckedImageVector = Icons.AutoMirrored.Outlined.VolumeUp,
            contentDescription = stringResource(R.string.RECITE),
            checked = audioOptionChecked,
            onClick = { audioOptionChecked = it }
        ),
        ToggleButtonItem(
            checkedIconId = R.drawable.bookmark_selected,
            uncheckedIconId = R.drawable.bookmark,
            contentDescription = stringResource(R.string.BOOKMARK),
            checked = isBookmarked,
            onClick = { poemViewModel.onBookmarkClicked() }
        ),
        ToggleButtonItem(
            checkedIconId = R.drawable.note,
            uncheckedIconId = R.drawable.note,
            contentDescription = stringResource(R.string.COMMENT),
            checked = showNotes,
            onClick = { showNotes = !showNotes }
        ),
        ToggleButtonItem(
            checkedImageVector = Icons.Default.MoreVert,
            uncheckedImageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.OPTIONS),
            checked = moreOptionsChecked,
            onClick = { moreOptionsChecked = it }
        )
    )

    val moreOptionsList = listOf(
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = stringResource(R.string.SHOW_HIGHLIGHT)
                )
            },
            activatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.VisibilityOff,
                    contentDescription = stringResource(R.string.DONT_SHOW_HIGHLIGHT)
                )
            },
            deactivatedText = stringResource(R.string.SHOW_HIGHLIGHT),
            activatedText = stringResource(R.string.DONT_SHOW_HIGHLIGHT),
            isActive = showHighlights,
            onClick = { showHighlights = !showHighlights; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Default.FormatListNumbered,
                    contentDescription = stringResource(R.string.VERSE_NUMBER)
                )
            },
            activatedIcon = {
                Icon(
                    imageVector = Icons.Default.FormatListNumbered,
                    contentDescription = stringResource(R.string.REMOVE_VERSE_NUMBER)
                )
            },
            deactivatedText = stringResource(R.string.VERSE_NUMBER),
            activatedText = stringResource(R.string.REMOVE_VERSE_NUMBER),
            isActive = showVerseNumbers,
            onClick = { showVerseNumbers = !showVerseNumbers; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    painter = painterResource(R.drawable.selection),
                    contentDescription = stringResource(R.string.SELECT_VERSES),
                )
            },
            deactivatedText = stringResource(R.string.SELECT_VERSES),
            isActive = selectMode,
            onClick = { selectMode = !selectMode; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.SHARE),
                )
            },
            deactivatedText = stringResource(R.string.SHARE),
            onClick = {
                moreOptionsChecked = !moreOptionsChecked; poemViewModel.share(
                versesWithHighlights,
                poemTitle,
                context
            )
            }
        )
    )

    val sheetState = rememberModalBottomSheetState()

    if (audioOptionChecked || moreOptionsChecked) {
        ModalBottomSheet(
            onDismissRequest = {
                audioOptionChecked = false
                moreOptionsChecked = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            if (audioOptionChecked) {
                AudioListItems(viewModel = poemViewModel, audioViewModel = audioViewModel, onDismiss={audioOptionChecked = false})
            } else if (moreOptionsChecked) {
                PoemMoreOptionsList(optionsList = moreOptionsList)
            }
        }
    }

    if (showNotes) {
        NotesBottomSheet(
            onDismissRequest = { showNotes = false },
            poemId = poemId,
        )
    }

    Column(
        modifier = modifier
    ) {
        Surface(
            shadowElevation = 4.dp
        ) {
            Column {
                path?.let {
                    PoemScreenTitle(
                        navController = navController,
                        minId = minId,
                        maxId = maxId,
                        poemPath = it,
                    )
                }

                PoemScreenActionHeader(
                    toggleButtonItems = toggleButtonItems
                )
            }
        }

        LazyColumn(modifier = Modifier.padding(10.dp), state = lazyListState) {
            itemsIndexed(versesWithHighlights) { index, verseWithHighlights ->
                val isSelected = selectedVerses.contains(verseWithHighlights)
                val itemModifier =
                    if (shouldFocusForSearch && index == versesWithHighlights.indexOf(focusedVerse)) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    } else if (shouldFocusForRecitation && index == recitedVerseIndex) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface) // TODO change color maybe?
                    } else if (isSelected) {
                        Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        Modifier
                    }

                VerseItem(
                    modifier = itemModifier,
                    verse = verseWithHighlights.verse,
                    highlights = if (showHighlights) verseWithHighlights.highlights else listOf(),
                    index = index,
                    showVerseNumber = showVerseNumbers,
                    selectMode = selectMode,
                    isSelected = isSelected,
                    onClick = {
                        if (selectMode) onClick(isSelected, verseWithHighlights)
                    },
                    verseStyle = verseStyle
                ) { startIndex, endIndex ->
                    poemViewModel.highlight(
                        verseWithHighlights.verse.id,
                        startIndex,
                        endIndex
                    )
                }

            }
        }
    }

    PoemScreenBottomToolBar(selectMode, modifier, selectedVerses) {
        selectMode = false
        selectedVerses.clear()
    }
}
