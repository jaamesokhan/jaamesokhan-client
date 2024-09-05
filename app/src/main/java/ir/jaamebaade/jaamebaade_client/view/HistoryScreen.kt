package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordWithPath
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel
import java.sql.Date

@Composable
fun HistoryScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val poemHistory = viewModel.poemHistory

    if (poemHistory.isEmpty()) {
        EmptyHistoryView(modifier)
    } else {
        HistoryList(viewModel = viewModel,
            navController = navController,
            poemHistory = poemHistory,
            modifier = modifier
        )
    }

}

@Composable
fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("از شعری بازدید نکرده‌اید!", style = MaterialTheme.typography.headlineMedium)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryList(
    viewModel: HistoryViewModel,
    navController: NavController,
    poemHistory: List<HistoryRecordWithPath>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(items = poemHistory, key = { it.id }) { item ->
            CardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .animateItemPlacement(),
                bodyText = buildAnnotatedString {
                    append(
                        item.path.toPathHeaderText()
                    )
                },
                footerText = Date(item.timestamp).convertToJalali().toLocalFormatWithHour(),
                onClick = { navController.navigate("${AppRoutes.POEM}/${item.path.poet.id}/${item.path.poem.id}/-1") },
                icon = Icons.Outlined.Delete,
                onIconClick = { viewModel.deleteHistoryRecord(item.id) },
            )
        }
    }
}

