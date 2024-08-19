package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet

@Composable
fun HighlightItem(
    modifier: Modifier = Modifier,
    highlightInfo: HighlightVersePoemCategoriesPoet,
    navController: NavController,
    onDeleteClicked: () -> Unit
) {
    val startIndex = highlightInfo.highlight.startIndex
    val endIndex = highlightInfo.highlight.endIndex
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("${AppRoutes.POEM}/${highlightInfo.versePath.poet.id}/${highlightInfo.versePath.poem.id}/${highlightInfo.versePath.verse.id}") }
    ) {

        PoemTracePath(versePoemCategoriesPoet = highlightInfo.versePath)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    highlightInfo.versePath.verse.text.substring(
                        startIndex,
                        endIndex
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        .padding(horizontal = 2.dp)
                )
            }

            IconButton(
                onClick = {
                    onDeleteClicked()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "حذف")
            }
        }
    }
}