package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemHistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen( navController: NavController, poemHistoryViewModel: PoemHistoryViewModel = hiltViewModel()) {
    val poemHistory = poemHistoryViewModel.poemHistory

    Scaffold()
    { padding ->
        if (poemHistory.isEmpty()) {
            EmptyHistoryView(modifier = Modifier.padding(padding))
        } else {
            PoemHistoryList(navController, poemHistory, modifier = Modifier.padding(padding))
        }
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
fun PoemHistoryList(navController: NavController, poemHistory: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(poemHistory) { (title, content) ->
            PoemHistoryItem(navController, title, content)
            HorizontalDivider()
        }
    }
}

@Composable
fun PoemHistoryItem(
    navController: NavController,
    timestamp: String,
    path: String
) {
    // Split the path by the "|" delimiter to extract the title, author, and content
    val poemDetails = path.split("/")
    val poetId = poemDetails.getOrNull(1) ?: ""
    val poemId = poemDetails.getOrNull(2) ?: ""
    val verseId = poemDetails.getOrNull(3)?.toIntOrNull() ?: -1
    val date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(timestamp.toLong()))

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                // Navigate to the specific poem
                navController.navigate("${AppRoutes.POEM}/${poetId}/${poemId}/${verseId}")
            }
    ) {
        Text("Visited on: $date", style = MaterialTheme.typography.titleMedium)
        Text("Poet ID: $poetId", style = MaterialTheme.typography.bodyMedium)
        Text("Poem ID: $poemId", style = MaterialTheme.typography.bodySmall)

            Text("Verse ID: $verseId", style = MaterialTheme.typography.bodySmall)

    }
}