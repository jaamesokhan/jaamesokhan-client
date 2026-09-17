package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.bookmark.BottomSheetListItem

@Composable
fun ItemActionsBottomSheet(
    onDismiss: () -> Unit,
    onAddToCategory: () -> Unit,
    onGoToPoem: () -> Unit,
    onShare: () -> Unit,
    onRemove: () -> Unit,
) {
    CustomBottomSheet(onDismissRequest = onDismiss) {
        Column {
            BottomSheetListItem(
                icon = Icons.Filled.Add,
                text = stringResource(R.string.ADD_TO_CATEGORY),
                onClick = onAddToCategory,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 20.dp, end = 5.dp)
            )
            BottomSheetListItem(
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                text = stringResource(R.string.GO_TO_POEM),
                onClick = onGoToPoem,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 20.dp, end = 5.dp)
            )
            BottomSheetListItem(
                icon = Icons.Outlined.Share,
                text = stringResource(R.string.SHARE),
                onClick = onShare,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 20.dp, end = 5.dp)
            )
            BottomSheetListItem(
                icon = ImageVector.vectorResource(R.drawable.delete),
                text = stringResource(R.string.REMOVE_FROM_LIST),
                contentColor = MaterialTheme.colorScheme.error,
                onClick = onRemove,
            )
        }
    }
}
