package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.BookmarkCategoryItem
import ir.jaamebaade.jaamebaade_client.model.MergedHighlight
import ir.jaamebaade.jaamebaade_client.utility.toPersianNumber
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.AddCategoryChip
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.CategoryFilterChip
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.CategoryPickerBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.ItemActionsBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.LabeledCardItem
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.ManageCategoriesBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.NewCategoryBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import ir.jaamebaade.jaamebaade_client.viewmodel.BookmarkCategoriesViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.BookmarkCategorySheetType
import ir.jaamebaade.jaamebaade_client.viewmodel.BookmarkCategoryTab
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun BookmarkCategoriesScreen(
    modifier: Modifier,
    navController: NavController,
    initialTab: String,
) {
    val tab = BookmarkCategoryTab.fromArg(initialTab)
    val viewModel = hiltViewModel<BookmarkCategoriesViewModel, BookmarkCategoriesViewModel.Factory> { factory ->
        factory.create(tab)
    }
    val context = LocalContext.current

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if (backStackEntry.destination.route == "${AppRoutes.BOOKMARK_CATEGORIES_SCREEN}/{tab}") {
                viewModel.refresh()
            }
        }
    }

    val visibleItems = viewModel.visibleItems

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.space14),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
        ) {
            item {
                CategoryFilterChip(
                    name = stringResource(R.string.ALL_CATEGORIES_CHIP),
                    count = viewModel.items.size.toPersianNumber(),
                    color = null,
                    selected = viewModel.activeFilterLabelId == null,
                    onClick = { viewModel.selectFilter(null) },
                )
            }
            items(viewModel.labels) { labelWithCount ->
                CategoryFilterChip(
                    name = labelWithCount.label.name,
                    count = labelWithCount.itemCount.toPersianNumber(),
                    color = labelWithCount.label.color,
                    selected = viewModel.activeFilterLabelId == labelWithCount.label.id,
                    onClick = { viewModel.selectFilter(labelWithCount.label.id) },
                )
            }
            item {
                AddCategoryChip(
                    text = stringResource(R.string.CATEGORIES),
                    onClick = { viewModel.openManage() },
                )
            }
        }

        if (visibleItems.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(Dimens.space10)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    contentDescription = "",
                )
                if (viewModel.activeFilterLabelId == null) {
                    Spacer(modifier = Modifier.width(Dimens.space8))
                    Text(
                        text = stringResource(
                            if (tab == BookmarkCategoryTab.SAVE) R.string.NO_BOOKMARK else R.string.NO_HIGHLIGHTS_FOUND
                        ),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                } else {
                    Spacer(modifier = Modifier.width(Dimens.space8))
                    Column {
                        Text(
                            text = stringResource(R.string.EMPTY_CATEGORY_TITLE),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Text(
                            text = stringResource(R.string.EMPTY_CATEGORY_SUBTITLE),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.space12),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = Dimens.space16),
            ) {
                items(items = visibleItems, key = { item -> item.id }) { item ->
                    val isHighlight = tab == BookmarkCategoryTab.HIGHLIGHT && item.highlightSource != null
                    val bodyText = if (isHighlight) {
                        createMergedHighlightItemHeader(item.highlightSource!!)
                    } else {
                        AnnotatedString(item.previewText)
                    }

                    LabeledCardItem(
                        modifier = Modifier.animateItem(),
                        pathText = item.pathText,
                        bodyText = bodyText,
                        bodyMaxLines = if (isHighlight) Int.MAX_VALUE else 1,
                        imageUrl = item.imageUrl,
                        labels = item.labels,
                        onClick = {
                            item.bookmarkSource?.let {
                                navController.navigate("${AppRoutes.POEM}/${it.poet.id}/${it.poem.id}/-1")
                            }
                            item.highlightSource?.let {
                                navController.navigate("${AppRoutes.POEM}/${it.poet.id}/${it.poem.id}/${it.highlights.first().verseId}")
                            }
                        },
                        onMoreClick = { viewModel.openItemActions(item.id) },
                        onAddToCategoryClick = { viewModel.openItemActions(item.id) },
                    )
                }
            }
        }
    }

    when (viewModel.sheet) {
        BookmarkCategorySheetType.ITEM_ACTIONS -> {
            ItemActionsBottomSheet(
                onDismiss = viewModel::closeSheet,
                onAddToCategory = viewModel::openPicker,
                onGoToPoem = {
                    viewModel.consumeGoToPoemTarget()?.let { (poetId, poemId, verseId) ->
                        navController.navigate("${AppRoutes.POEM}/$poetId/$poemId/$verseId")
                    }
                },
                onShare = { viewModel.share(context) },
                onRemove = {
                    viewModel.removeItem()
                    ToastManager.showToast(R.string.UNBOOKMARK_SUCCESS, ToastType.SUCCESS)
                },
            )
        }

        BookmarkCategorySheetType.PICKER -> {
            CategoryPickerBottomSheet(
                labels = viewModel.labels,
                selected = viewModel.stagedPickerSelection,
                onToggle = viewModel::togglePickerSelection,
                onCreateNew = { viewModel.openNewCategory(null) },
                onCancel = viewModel::cancelPicker,
                onConfirm = viewModel::confirmPicker,
            )
        }

        BookmarkCategorySheetType.NEW_CATEGORY -> {
            NewCategoryBottomSheet(
                isEditing = viewModel.editingLabel != null,
                name = viewModel.draftName,
                onNameChange = viewModel::onDraftNameChange,
                color = viewModel.draftColor,
                onColorChange = viewModel::onDraftColorChange,
                onCancel = viewModel::cancelNewCategory,
                onSave = {
                    val wasEditing = viewModel.editingLabel != null
                    viewModel.saveNewCategory()
                    ToastManager.showToast(
                        if (wasEditing) R.string.CATEGORY_EDITED_SUCCESS else R.string.CATEGORY_CREATED_SUCCESS,
                        ToastType.SUCCESS
                    )
                },
            )
        }

        BookmarkCategorySheetType.MANAGE -> {
            ManageCategoriesBottomSheet(
                title = stringResource(
                    if (tab == BookmarkCategoryTab.SAVE) R.string.MANAGE_CATEGORIES_SAVE else R.string.MANAGE_CATEGORIES_HIGHLIGHT
                ),
                labels = viewModel.labels,
                onEdit = { viewModel.openNewCategory(it.label) },
                onDelete = {
                    viewModel.deleteLabel(it.label)
                    ToastManager.showToast(R.string.CATEGORY_DELETED_SUCCESS, ToastType.SUCCESS)
                },
                onCreateNew = { viewModel.openNewCategory(null) },
                onDismiss = viewModel::closeSheet,
            )
        }

        BookmarkCategorySheetType.NONE -> Unit
    }
}

private fun createMergedHighlightItemHeader(mergedHighlight: MergedHighlight): AnnotatedString {
    var res = AnnotatedString("")
    mergedHighlight.highlights.zip(mergedHighlight.verses)
        .forEach { (highlight, verse) ->
            val annotatedString = buildAnnotatedString {
                if (highlight == mergedHighlight.highlights.last()) {
                    append(verse.text)
                } else {
                    append(verse.text.plus("\n"))
                }
                addStyle(
                    style = SpanStyle(background = Color(highlight.color)),
                    start = highlight.startIndex,
                    end = highlight.endIndex
                )
            }
            res = res.plus(annotatedString)
        }
    return res
}
