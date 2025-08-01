package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordWithPath
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
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
    onDismiss: () -> Unit,
    sheetState: SheetState,
) {
    val poemHistory = viewModel.poemHistory

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.CLOSE),
                    tint = MaterialTheme.colorScheme.neutralN50
                )
            }
            Text(
                text = stringResource(R.string.READ_HISTORY),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.neutralN50,
            )
        }

        if (poemHistory.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.NO_HISTORY),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = poemHistory, key = { _, item -> item.id }) { index, historyRecord ->
                    MyHistoryCardItem(
                        historyRecord = historyRecord
                    ) {
                        navController.navigate("${AppRoutes.POEM}/${historyRecord.path.poet.id}/${historyRecord.path.poem.id}/-1")
                        onDismiss()
                    }

                    if (index != poemHistory.size - 1)
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                start = 90.dp,
                                end = 0.dp,
                                top = 5.dp,
                                bottom = 5.dp
                            ),
                            color = MaterialTheme.colorScheme.outline
                        )
                }
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
    if (poemHistory.isEmpty()) {

    } else
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(items = poemHistory, key = { _, item -> item.id }) { index, item ->
                CardItem(
                    modifier = Modifier.animateItem(),
                    bodyText = AnnotatedString(item.path.toPathHeaderText()),
                    footerText = Date(item.timestamp).convertToJalali().toLocalFormatWithHour(),
                    onClick = { navController.navigate("${AppRoutes.POEM}/${item.path.poet.id}/${item.path.poem.id}/-1") },
                    icon = Icons.Outlined.Delete,
                    onIconClick = { viewModel.deleteHistoryRecord(item.id) },
                )
                if (index != poemHistory.size - 1)
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = 90.dp,
                            end = 0.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                        color = MaterialTheme.colorScheme.outline
                    )

            }
        }
}

