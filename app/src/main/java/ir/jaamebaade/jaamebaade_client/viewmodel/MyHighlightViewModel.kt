package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.MergedHighlight
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MyHighlightViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val highlightRepository: HighlightRepository
) : ViewModel() {


    var highlights by mutableStateOf<List<HighlightVersePoemCategoriesPoet>>(emptyList())
        private set

    init {
        getAllHighlights()
    }

    fun deleteHighlight(highlight: Highlight) {
        viewModelScope.launch {
            highlights = highlights.toMutableList().filterNot { it.highlight.id == highlight.id }
            deleteHighlightFromRepository(highlight)
        }
    }


    private suspend fun deleteHighlightFromRepository(highlight: Highlight) {
        withContext(Dispatchers.IO) {
            highlightRepository.deleteHighlight(highlight)
        }
    }


    private fun getAllHighlights() {
        viewModelScope.launch {
            highlights = getHighlightsWithVersePoemPoetFromRepository()
        }
    }


    private suspend fun getHighlightsWithVersePoemPoetFromRepository(): List<HighlightVersePoemCategoriesPoet> {
        val res = withContext(Dispatchers.IO) {
            highlightRepository.getAllHighlightsWithVersePoemPoet().map {
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
        }
        return res
    }

    fun share(highlight: MergedHighlight, context: Context) {
        viewModelScope.launch {
            val verses = highlight.verses
            val textToCopy =
                verses.joinToString("\n") { it.text }.plus("\n\n${highlight.poet.name}")
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToCopy)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    fun refreshHighlights() {
        getAllHighlights()
    }

}