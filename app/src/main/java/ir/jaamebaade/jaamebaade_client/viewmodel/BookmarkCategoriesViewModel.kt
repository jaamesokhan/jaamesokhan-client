package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.BookmarkCategoryItem
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoetFirstVerse
import ir.jaamebaade.jaamebaade_client.model.CATEGORY_COLOR_PALETTE
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.model.LabelType
import ir.jaamebaade.jaamebaade_client.model.LabelWithCount
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.toMergedHighlight
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import ir.jaamebaade.jaamebaade_client.repository.LabelRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class BookmarkCategoryTab(val arg: String) {
    SAVE("save"),
    HIGHLIGHT("hi");

    companion object {
        fun fromArg(arg: String?): BookmarkCategoryTab =
            entries.find { it.arg == arg } ?: SAVE
    }
}

enum class BookmarkCategorySheetType {
    NONE, ITEM_ACTIONS, PICKER, NEW_CATEGORY, MANAGE
}

@HiltViewModel(assistedFactory = BookmarkCategoriesViewModel.Factory::class)
class BookmarkCategoriesViewModel @AssistedInject constructor(
    @Assisted val tab: BookmarkCategoryTab,
    private val bookmarkRepository: BookmarkRepository,
    private val highlightRepository: HighlightRepository,
    private val labelRepository: LabelRepository,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
    private val verseRepository: VerseRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(tab: BookmarkCategoryTab): BookmarkCategoriesViewModel
    }

    private val labelType = when (tab) {
        BookmarkCategoryTab.SAVE -> LabelType.BOOKMARK
        BookmarkCategoryTab.HIGHLIGHT -> LabelType.HIGHLIGHT
    }

    var labels by mutableStateOf<List<LabelWithCount>>(emptyList())
        private set

    var items by mutableStateOf<List<BookmarkCategoryItem>>(emptyList())
        private set

    var activeFilterLabelId by mutableStateOf<Int?>(null)
        private set

    var sheet by mutableStateOf(BookmarkCategorySheetType.NONE)
        private set

    private var activeItemId by mutableStateOf<Int?>(null)

    var stagedPickerSelection by mutableStateOf<Set<Int>>(emptySet())
        private set

    var editingLabel by mutableStateOf<Label?>(null)
        private set

    var draftName by mutableStateOf("")
        private set

    var draftColor by mutableStateOf(CATEGORY_COLOR_PALETTE.first())
        private set

    val visibleItems: List<BookmarkCategoryItem>
        get() = activeFilterLabelId?.let { labelId ->
            items.filter { item -> item.labels.any { it.id == labelId } }
        } ?: items

    val activeItem: BookmarkCategoryItem?
        get() = items.find { it.id == activeItemId }

    init {
        refresh()
    }

    fun refresh() {
        loadLabels()
        loadItems()
    }

    private fun loadLabels() {
        viewModelScope.launch {
            labels = withContext(Dispatchers.IO) { labelRepository.getLabelCounts(labelType) }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            items = withContext(Dispatchers.IO) {
                when (tab) {
                    BookmarkCategoryTab.SAVE -> loadBookmarkItems()
                    BookmarkCategoryTab.HIGHLIGHT -> loadHighlightItems()
                }
            }
        }
    }

    private fun loadBookmarkItems(): List<BookmarkCategoryItem> {
        val labelsByBookmarkId =
            labelRepository.getBookmarksWithLabels().associate { it.bookmark.id to it.labels }
        return bookmarkRepository.getAllBookmarksWithPoemAndPoet().map { bpp ->
            val categories = categoryRepository.getAllParentsOfCategoryId(bpp.poem.categoryId)
            val firstVerse = poemRepository.getPoemFirstVerse(bpp.poem.id)
            val source = BookmarkPoemCategoriesPoetFirstVerse(
                bookmark = bpp.bookmark,
                poem = bpp.poem,
                poet = bpp.poet,
                categories = categories,
                firstVerse = firstVerse,
            )
            BookmarkCategoryItem(
                id = source.bookmark.id,
                bookmarkSource = source,
                pathText = "${categories.joinToString(" > ") { it.text }} > ${source.poem.title}",
                previewText = firstVerse.text,
                imageUrl = source.poet.imageUrl,
                labels = labelsByBookmarkId[source.bookmark.id] ?: emptyList(),
            )
        }
    }

    private fun loadHighlightItems(): List<BookmarkCategoryItem> {
        val labelsByHighlightId =
            labelRepository.getHighlightsWithLabels().associate { it.highlight.id to it.labels }
        val raw = highlightRepository.getAllHighlightsWithVersePoemPoet().map {
            HighlightVersePoemCategoriesPoet(
                highlight = it.highlight,
                versePath = VersePoemCategoriesPoet(
                    verse = it.verse,
                    poem = it.poem,
                    poet = it.poet,
                    categories = categoryRepository.getAllParentsOfCategoryId(it.poem.categoryId),
                )
            )
        }
        if (raw.isEmpty()) return emptyList()

        val sorted = raw.sortedBy { it.versePath.verse!!.id }
        val merged = mutableListOf(sorted[0].toMergedHighlight())
        for (i in 1 until sorted.size) {
            val last = merged.last()
            if (sorted[i].versePath.verse!!.id == last.highlights.last().verseId + 1L &&
                sorted[i].versePath.poem.id == last.poem.id
            ) {
                last.highlights.add(sorted[i].highlight)
                last.verses.add(sorted[i].versePath.verse!!)
            } else {
                merged.add(sorted[i].toMergedHighlight())
            }
        }
        val ordered = merged.sortedBy { it.highlights.first().createdAt }

        return ordered.map { mh ->
            val unionLabels =
                mh.highlights.flatMap { labelsByHighlightId[it.id] ?: emptyList() }.distinctBy { it.id }
            BookmarkCategoryItem(
                id = mh.highlights.first().id,
                highlightSource = mh,
                pathText = "${mh.categories.joinToString(" > ") { it.text }} > ${mh.poem.title}",
                previewText = "",
                imageUrl = mh.poet.imageUrl,
                labels = unionLabels,
            )
        }
    }

    fun selectFilter(labelId: Int?) {
        activeFilterLabelId = labelId
    }

    fun openItemActions(itemId: Int) {
        activeItemId = itemId
        sheet = BookmarkCategorySheetType.ITEM_ACTIONS
    }

    fun openPicker() {
        val item = activeItem ?: return
        stagedPickerSelection = item.labels.map { it.id }.toSet()
        sheet = BookmarkCategorySheetType.PICKER
    }

    fun togglePickerSelection(labelId: Int) {
        stagedPickerSelection =
            if (labelId in stagedPickerSelection) stagedPickerSelection - labelId
            else stagedPickerSelection + labelId
    }

    fun confirmPicker() {
        val item = activeItem ?: return
        val before = item.labels.map { it.id }.toSet()
        val toAdd = stagedPickerSelection - before
        val toRemove = before - stagedPickerSelection
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                toAdd.forEach { assignLabelToItem(item, it) }
                toRemove.forEach { unassignLabelFromItem(item, it) }
            }
            closeSheet()
            refresh()
        }
    }

    fun cancelPicker() {
        closeSheet()
    }

    fun openManage() {
        activeItemId = null
        sheet = BookmarkCategorySheetType.MANAGE
    }

    fun openNewCategory(editing: Label?) {
        editingLabel = editing
        draftName = editing?.name ?: ""
        draftColor = editing?.color ?: CATEGORY_COLOR_PALETTE.first()
        sheet = BookmarkCategorySheetType.NEW_CATEGORY
    }

    fun cancelNewCategory() {
        editingLabel = null
        sheet = if (activeItemId != null) BookmarkCategorySheetType.PICKER else BookmarkCategorySheetType.MANAGE
    }

    fun onDraftNameChange(value: String) {
        draftName = value
    }

    fun onDraftColorChange(value: String) {
        draftColor = value
    }

    fun saveNewCategory() {
        val name = draftName.trim()
        if (name.isEmpty()) return
        viewModelScope.launch {
            val editing = editingLabel
            withContext(Dispatchers.IO) {
                if (editing != null) {
                    labelRepository.updateLabel(editing.copy(name = name, color = draftColor))
                } else {
                    val created = labelRepository.createLabel(name, draftColor, labelType)
                    if (activeItemId != null) {
                        stagedPickerSelection = stagedPickerSelection + created.id
                    }
                }
            }
            editingLabel = null
            draftName = ""
            draftColor = CATEGORY_COLOR_PALETTE.first()
            sheet = if (activeItemId != null) BookmarkCategorySheetType.PICKER else BookmarkCategorySheetType.MANAGE
            loadLabels()
        }
    }

    fun deleteLabel(label: Label) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { labelRepository.deleteLabel(label) }
            if (activeFilterLabelId == label.id) activeFilterLabelId = null
            refresh()
        }
    }

    fun removeItem() {
        val item = activeItem ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                item.bookmarkSource?.let { bookmarkRepository.removeBookmark(it.poem.id) }
                item.highlightSource?.highlights?.forEach { highlightRepository.deleteHighlight(it) }
            }
            closeSheet()
            loadItems()
        }
    }

    fun consumeGoToPoemTarget(): Triple<Int, Int, Long>? {
        val item = activeItem
        closeSheet()
        return item?.bookmarkSource?.let { Triple(it.poet.id, it.poem.id, -1L) }
            ?: item?.highlightSource?.let { Triple(it.poet.id, it.poem.id, it.highlights.first().verseId) }
    }

    fun share(context: Context) {
        val item = activeItem ?: return
        viewModelScope.launch {
            val text = withContext(Dispatchers.IO) {
                item.bookmarkSource?.let { bs ->
                    val verses = verseRepository.getPoemVersesWithHighlights(bs.poem.id)
                    verses.joinToString("\n") { it.verse.text }.plus("\n\n${bs.poet.name}")
                } ?: item.highlightSource?.let { hs ->
                    hs.verses.joinToString("\n") { it.text }.plus("\n\n${hs.poet.name}")
                } ?: ""
            }
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
            closeSheet()
        }
    }

    fun closeSheet() {
        sheet = BookmarkCategorySheetType.NONE
        activeItemId = null
        editingLabel = null
    }

    private fun assignLabelToItem(item: BookmarkCategoryItem, labelId: Int) {
        item.bookmarkSource?.let { labelRepository.assignBookmarkLabel(it.bookmark.id, labelId) }
        item.highlightSource?.highlights?.forEach { labelRepository.assignHighlightLabel(it.id, labelId) }
    }

    private fun unassignLabelFromItem(item: BookmarkCategoryItem, labelId: Int) {
        item.bookmarkSource?.let { labelRepository.unassignBookmarkLabel(it.bookmark.id, labelId) }
        item.highlightSource?.highlights?.forEach { labelRepository.unassignHighlightLabel(it.id, labelId) }
    }
}
