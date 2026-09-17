package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.CATEGORY_COLOR_PALETTE
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.model.LabelType
import ir.jaamebaade.jaamebaade_client.repository.LabelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class SaveMomentSheet { NONE, NEW_CATEGORY }

@HiltViewModel
class SaveMomentViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
) : ViewModel() {

    var labels by mutableStateOf<List<Label>>(emptyList())
        private set

    var selected by mutableStateOf<Set<Int>>(emptySet())
        private set

    var sheet by mutableStateOf(SaveMomentSheet.NONE)
        private set

    var draftName by mutableStateOf("")
        private set

    var draftColor by mutableStateOf(CATEGORY_COLOR_PALETTE.first())
        private set

    fun load() {
        viewModelScope.launch {
            labels = withContext(Dispatchers.IO) { labelRepository.getLabels(LabelType.BOOKMARK) }
            selected = emptySet()
        }
    }

    fun toggleSelection(labelId: Int) {
        selected = if (labelId in selected) selected - labelId else selected + labelId
    }

    fun openNewCategory() {
        draftName = ""
        draftColor = CATEGORY_COLOR_PALETTE.first()
        sheet = SaveMomentSheet.NEW_CATEGORY
    }

    fun cancelNewCategory() {
        sheet = SaveMomentSheet.NONE
    }

    fun onDraftNameChange(value: String) {
        draftName = value
    }

    fun onDraftColorChange(value: String) {
        draftColor = value
    }

    fun createLabel() {
        val name = draftName.trim()
        if (name.isEmpty()) return
        viewModelScope.launch {
            val created = withContext(Dispatchers.IO) {
                labelRepository.createLabel(name, draftColor, LabelType.BOOKMARK)
            }
            labels = labels + created
            selected = selected + created.id
            sheet = SaveMomentSheet.NONE
        }
    }

    fun confirm(bookmarkId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                selected.forEach { labelId -> labelRepository.assignBookmarkLabel(bookmarkId, labelId) }
            }
            onDone()
        }
    }
}
