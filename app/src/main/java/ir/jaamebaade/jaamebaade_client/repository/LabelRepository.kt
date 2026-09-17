package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.BookmarkLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.BookmarkWithLabels
import ir.jaamebaade.jaamebaade_client.model.HighlightLabelCrossRef
import ir.jaamebaade.jaamebaade_client.model.HighlightWithLabels
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.model.LabelType
import ir.jaamebaade.jaamebaade_client.model.LabelWithCount
import javax.inject.Inject

class LabelRepository @Inject constructor(appDatabase: AppDatabase) {
    private val labelDao = appDatabase.labelDao()
    private val bookmarkLabelCrossRefDao = appDatabase.bookmarkLabelCrossRefDao()
    private val highlightLabelCrossRefDao = appDatabase.highlightLabelCrossRefDao()

    fun getLabels(type: LabelType): List<Label> = labelDao.getLabelsByType(type.value)

    fun getLabelCounts(type: LabelType): List<LabelWithCount> = when (type) {
        LabelType.BOOKMARK -> bookmarkLabelCrossRefDao.getLabelCounts()
        LabelType.HIGHLIGHT -> highlightLabelCrossRefDao.getLabelCounts()
    }

    fun createLabel(name: String, color: String, type: LabelType): Label {
        val label = Label(name = name, color = color, type = type.value)
        val id = labelDao.insertLabel(label)
        return label.copy(id = id.toInt())
    }

    fun updateLabel(label: Label) = labelDao.updateLabel(label)

    fun deleteLabel(label: Label) = labelDao.deleteLabel(label)

    fun assignBookmarkLabel(bookmarkId: Int, labelId: Int) =
        bookmarkLabelCrossRefDao.assign(BookmarkLabelCrossRef(bookmarkId, labelId))

    fun unassignBookmarkLabel(bookmarkId: Int, labelId: Int) =
        bookmarkLabelCrossRefDao.unassign(bookmarkId, labelId)

    fun assignHighlightLabel(highlightId: Int, labelId: Int) =
        highlightLabelCrossRefDao.assign(HighlightLabelCrossRef(highlightId, labelId))

    fun unassignHighlightLabel(highlightId: Int, labelId: Int) =
        highlightLabelCrossRefDao.unassign(highlightId, labelId)

    fun getBookmarksWithLabels(): List<BookmarkWithLabels> =
        bookmarkLabelCrossRefDao.getBookmarksWithLabels()

    fun getHighlightsWithLabels(): List<HighlightWithLabels> =
        highlightLabelCrossRefDao.getHighlightsWithLabels()
}
