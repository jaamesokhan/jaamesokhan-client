package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemHistoryViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun HistoryScreen(modifier: Modifier, navController: NavController, poemHistoryViewModel: PoemHistoryViewModel = hiltViewModel()) {
    val poemHistory = poemHistoryViewModel.poemHistory

    if (poemHistory.isEmpty()) {
        EmptyHistoryView(modifier)
    } else {
        PoemHistoryList(poemHistoryViewModel, navController, poemHistory, modifier)
    }

}

@Composable
fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No poems visited yet", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PoemHistoryList(poemHistoryViewModel: PoemHistoryViewModel,navController: NavController, poemHistory: List<PoemWithPoet>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(poemHistory) { poemWithPoet ->
            PoemHistoryItem(navController, poemWithPoet)
            HorizontalDivider()
        }
    }
}

@Composable
fun PoemHistoryItem(
    navController: NavController,
    poemWithPoet: PoemWithPoet
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                navController.navigate("${AppRoutes.POEM}/${poemWithPoet.poet.id}/${poemWithPoet.poem.id}/-1")
            },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.Article, contentDescription = "شعر")
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text ="${poemWithPoet.poet.name} > ${poemWithPoet.poem.title}", style = MaterialTheme.typography.headlineMedium)
        }
    }

}