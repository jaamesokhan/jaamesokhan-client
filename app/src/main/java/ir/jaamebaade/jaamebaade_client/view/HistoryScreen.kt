package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordWithPath
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.view.components.MyHistoryCardItem
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel
import java.sql.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//fun HistoryScreen(
//    modifier: Modifier,
//    navController: NavController,
//    viewModel: HistoryViewModel = hiltViewModel()
//) {
//    val poemHistory = viewModel.poemHistory
//
//    if (poemHistory.isEmpty()) {
//        EmptyHistoryView(modifier)
//    } else {
//        HistoryList(viewModel = viewModel,
//            navController = navController,
//            poemHistory = poemHistory,
//            modifier = modifier
//        )
//    }
//
//}

fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
) {
    val poemHistory = viewModel.poemHistory

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.neutralN95,
    ) {
        val context = LocalContext.current
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(
                items = poemHistory, key = { _, item -> item.id }) { _, historyRecord ->
                MyHistoryCardItem(
                    historyRecord = historyRecord
                ) { navController.navigate("${AppRoutes.POEM}/${historyRecord.path.poet.id}/${historyRecord.path.poem.id}/-1") }
            }
        }


    }



}

@Composable
fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
        modifier = modifier.fillMaxSize()
    ) {
        items(items = poemHistory, key = { it.id }) { item ->
            CardItem(
                modifier = Modifier.animateItem(),
                bodyText = AnnotatedString(item.path.toPathHeaderText()),
                footerText = Date(item.timestamp).convertToJalali().toLocalFormatWithHour(),
                onClick = { navController.navigate("${AppRoutes.POEM}/${item.path.poet.id}/${item.path.poem.id}/-1") },
                icon = Icons.Outlined.Delete,
                onIconClick = { viewModel.deleteHistoryRecord(item.id) },
            )
        }
    }
}

