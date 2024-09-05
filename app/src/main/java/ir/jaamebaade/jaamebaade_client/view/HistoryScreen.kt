package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.model.VisitHistoryViewItem
import ir.jaamebaade.jaamebaade_client.view.components.HistoryListItem
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier,
    navController: NavController,
    poemHistoryViewModel: HistoryViewModel = hiltViewModel()
) {
    val poemHistory = poemHistoryViewModel.poemHistory

    if (poemHistory.isEmpty()) {
        EmptyHistoryView(modifier)
    } else {
        HistoryList(historyViewModel = poemHistoryViewModel, navController, poemHistory, modifier)
    }

}

@Composable
fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("از شعری بازدید نکرده‌اید!", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun HistoryList(
    historyViewModel: HistoryViewModel,
    navController: NavController,
    poemHistory: List<VisitHistoryViewItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(poemHistory) { item ->
            HistoryListItem(historyViewModel, navController, item)
        }
    }
}

