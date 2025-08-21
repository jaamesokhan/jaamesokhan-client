package ir.jaamebaade.jaamebaade_client.view


import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.LoadingIndicator
import ir.jaamebaade.jaamebaade_client.view.components.DownloadablePoetItem
import ir.jaamebaade.jaamebaade_client.view.components.ServerFailure
import ir.jaamebaade.jaamebaade_client.view.components.base.RectangularButton
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage
import ir.jaamebaade.jaamebaade_client.viewmodel.PoetViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun DownloadablePoetsScreen(
    modifier: Modifier = Modifier,
    poetViewModel: PoetViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = poetViewModel.poets
    val fetchStatus = poetViewModel.poetFetchStatus
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {

        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                poetViewModel.updateSearchQuery(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(Color.Transparent),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {

                IconButton(onClick = {

                    searchQuery = ""
                    poetViewModel.updateSearchQuery(searchQuery)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = stringResource(
                            R.string.CLOSE
                        )
                    )
                }

            },
            placeholder = {
                Text(
                    stringResource(R.string.POET_SEARCH_BAR_HINT),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
            }),
        )
        if (fetchStatus == Status.LOADING && poets.isEmpty()) {
            LoadingIndicator()
        } else if (fetchStatus == Status.FAILED) {
            ServerFailure()
        } else if (fetchStatus == Status.SUCCESS && poets.isEmpty()) {
            EmptyPoetResult()
        } else {
            Column {
                DownloadablePoetsList(poetViewModel, poets, fetchStatus, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadablePoetsList(
    viewModel: PoetViewModel,
    poets: List<Poet>,
    fetchStatus: Status,
    navController: NavController
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var clickedPoet by remember { mutableStateOf<Poet?>(null) }

    LazyColumn(userScrollEnabled = true, state = listState) {
        itemsIndexed(poets) { index, poet ->
            DownloadablePoetItem(
                poet = poet,
                viewModel.downloadStatus[poet.id.toString()] ?: DownloadStatus.NotDownloaded,
                onButtonClick =
                    if (viewModel.downloadStatus[poet.id.toString()] == DownloadStatus.Downloaded) {
                        {
                            coroutineScope.launch {
                                val poetCategoryId =
                                    viewModel.getPoetCategoryId(poet.id)
                                navController.navigate(
                                    "${AppRoutes.POET_CATEGORY_SCREEN}/${poet.id}/${
                                        intArrayOf(
                                            poetCategoryId
                                        ).toNavArgs()
                                    }"
                                )
                            }
                        }

                    } else {
                        {
                            coroutineScope.launch {
                                val targetDir = getInternalStorageDir(context)
                                viewModel.importPoetData(poet.id.toString(), targetDir)
                            }
                        }
                    },
                onItemClick = {
                    showBottomSheet = true
                    clickedPoet = poet
                })
            if (index != poets.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = 90.dp,
                        end = 0.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    ),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        item {
            if (fetchStatus == Status.LOADING) {
                LoadingIndicator()
            }
        }
    }
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == viewModel.poets.size - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadMorePoets()
        }
    }

    if (showBottomSheet) {
        DownloadablePoetOptionsBottomSheet(
            poet = clickedPoet!!,
            onDismiss = { showBottomSheet = false },
            sheetState = bottomSheetState,
            buttonType =
                if (viewModel.downloadStatus[clickedPoet!!.id.toString()] == DownloadStatus.Downloaded) {
                    DownloadPoetButtonType.OPEN
                } else {
                    DownloadPoetButtonType.ADD
                },
            onButtonClick =
                if (viewModel.downloadStatus[clickedPoet!!.id.toString()] == DownloadStatus.Downloaded) {
                    {
                        coroutineScope.launch {
                            val poetCategoryId =
                                viewModel.getPoetCategoryId(clickedPoet!!.id)
                            navController.navigate(
                                "${AppRoutes.POET_CATEGORY_SCREEN}/${clickedPoet!!.id}/${
                                    intArrayOf(
                                        poetCategoryId
                                    ).toNavArgs()
                                }"
                            )
                            showBottomSheet = false
                        }
                    }
                } else {
                    {
                        coroutineScope.launch {
                            val targetDir = getInternalStorageDir(context)
                            viewModel.importPoetData(clickedPoet!!.id.toString(), targetDir)
                        }
                        showBottomSheet = false
                    }
                })

    }
}

@Composable
private fun EmptyPoetResult() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.NO_POET_FOUND))
    }
}

private fun getInternalStorageDir(context: Context): File {
    val dir = File(context.filesDir, "poets")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return dir
}

enum class DownloadPoetButtonType {
    ADD, OPEN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadablePoetOptionsBottomSheet(
    poet: Poet,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    showHeader: Boolean = true,
    showDescription: Boolean = true,
    buttonType: DownloadPoetButtonType = DownloadPoetButtonType.ADD,
    onButtonClick: () -> Unit
) {
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.neutralN70) },
        containerColor = if (showHeader) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            if (showHeader) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .background(color = MaterialTheme.colorScheme.surface),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.CLOSE),
                            tint = MaterialTheme.colorScheme.neutralN50
                        )
                    }
                    Text(
                        text = stringResource(R.string.DOWNLOAD_NEW_POET),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.neutralN50,
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    SquareImage(
                        imageUrl = poet.imageUrl ?: stringResource(R.string.FALLBACK_IMAGE_URL),
                        contentDescription = poet.name,
                        size = 89,
                    )
                    Text(
                        text = poet.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp),
                    )
                    if (buttonType == DownloadPoetButtonType.ADD) {
                        RectangularButton(
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            textColor = MaterialTheme.colorScheme.primary,
                            borderColor = MaterialTheme.colorScheme.primary,
                            text = stringResource(R.string.DOWNLOAD_POET),
                            onClick = onButtonClick,
                        )
                    } else {
                        RectangularButton(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.onPrimary,
                            text = stringResource(R.string.OPEN_DOWNLOADED_POET_DESC),
                            onClick = onButtonClick,
                        )
                    }
                }

                if (showDescription) {
                    Text(
                        text = poet.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp),
                    )
                }
            }
        }
    }
}