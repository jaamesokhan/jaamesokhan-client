package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemPoet

@Composable
fun HighlightItem(
    modifier: Modifier = Modifier,
    highlightVersePoemPoet: HighlightVersePoemPoet,
    navController: NavController,
    onDeleteClicked: () -> Unit
) {
    val startIndex = highlightVersePoemPoet.highlight.startIndex
    val endIndex = highlightVersePoemPoet.highlight.endIndex
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { navController.navigate("${AppRoutes.POEM}/${highlightVersePoemPoet.poet.id}/${highlightVersePoemPoet.poem.id}/${highlightVersePoemPoet.verse.id}") }

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.9f)
            ) {
                Text(
                    text = "${highlightVersePoemPoet.poet.name}>${highlightVersePoemPoet.poem.title}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    highlightVersePoemPoet.verse.text.substring(startIndex, endIndex),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                )
            }

            Box(modifier = Modifier
                .padding(8.dp)
                .weight(0.1f)) {
                IconButton(
                    onClick = {
                        onDeleteClicked()
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "حذف")
                }
            }
        }
    }
}