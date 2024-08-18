package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet

@Composable
fun PoemTracePath(searchResult: VersePoemCategoriesPoet) {
    Text(
        text = "${searchResult.poet.name}>${
            searchResult.categories.joinToString(separator = ">") { it.text }
        }>${searchResult.poem.title}",
        style = MaterialTheme.typography.labelMedium
    )
}
