package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.VisitHistoryViewItem
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel
import java.sql.Date

@Composable
fun HistoryListItem(
    historyViewModel: HistoryViewModel,
    navController: NavController,
    item: VisitHistoryViewItem,
) {
    CardItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        bodyText = buildAnnotatedString {
            append(
                "${
                    item.versePoemCategoriesPoet.categories.joinToString(
                        separator = " > "
                    ) { it.text }
                } > ${item.versePoemCategoriesPoet.poem.title}"
            )
        },
        footerText = Date(item.timestamp).convertToJalali().toLocalFormatWithHour(),
        onClick = { navController.navigate("${AppRoutes.POEM}/${item.versePoemCategoriesPoet.poet.id}/${item.versePoemCategoriesPoet.poem.id}/-1") },
        icon = Icons.Outlined.Delete,
        onIconClick = { historyViewModel.onDeleteItemFromHistoryClicked(item.id) },
        )


}
