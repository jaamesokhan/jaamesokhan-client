package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet

@Composable
fun SearchResultItem(
    modifier: Modifier,
    result: VersePoemCategoriesPoet,
    searchQuery: String,
    navController: NavController
) {
    val text = result.verse.text
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append(text)
        }
        text.indexOf(searchQuery, ignoreCase = true).let { index ->
            if (index >= 0) {
                addStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    ),
                    start = index,
                    end = index + searchQuery.length
                )
            }
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("${AppRoutes.POEM}/${result.poet.id}/${result.verse.poemId}/${result.verse.id}")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PoemTracePath(searchResult = result)

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}