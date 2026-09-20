package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.JaamebaadeclientTheme

@Composable
internal fun rememberSampleRandomPoemPreview(): RandomPoemPreview {
    val poetName = stringResource(R.string.RANDOM_POEM_SAMPLE_POET)
    val bookName = stringResource(R.string.RANDOM_POEM_SAMPLE_BOOK)
    val firstHemistich = stringResource(R.string.SETTING_VERSE_ONE_1)
    val secondHemistich = stringResource(R.string.SETTING_VERSE_ONE_2)
    return remember(poetName, bookName, firstHemistich, secondHemistich) {
        val poet = Poet(id = 1, name = poetName, description = "", imageUrl = null)
        val category = Category(id = 1, text = bookName, parentId = 0, poetId = poet.id)
        val poem = Poem(id = 1, title = bookName, categoryId = category.id)
        RandomPoemPreview(
            poemPath = VersePoemCategoriesPoet(
                verse = null,
                poem = poem,
                categories = listOf(category),
                poet = poet,
            ),
            verses = listOf(
                Verse(id = 1, text = firstHemistich, verseOrder = 0, position = 0, poemId = poem.id),
                Verse(id = 2, text = secondHemistich, verseOrder = 1, position = 1, poemId = poem.id),
            ),
        )
    }
}

@Composable
internal fun RandomPoemPreviewFrame(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    JaamebaadeclientTheme(darkTheme = darkTheme) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Surface(color = MaterialTheme.colorScheme.background) {
                Box(modifier = Modifier.padding(horizontal = Dimens.space16)) {
                    content()
                }
            }
        }
    }
}
