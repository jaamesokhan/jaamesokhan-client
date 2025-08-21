package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetOptionsBottomSheet(
    poet: Poet,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    showHeader: Boolean = true,
    showDescription: Boolean = true,
    onDeleteClick: () -> Unit
) {
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.neutralN70) },
        containerColor = if (showHeader) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss, sheetState = sheetState
    ) {
        PoetBottomSheetContent(
            poet = poet,
            showHeader = showHeader,
            showDescription = showDescription,
            headerText = stringResource(R.string.DELETE_POET),
            buttonType = PoetInfoButtonType.DELETE,
            onButtonClick = onDeleteClick,
            buttonBackgroundColor = MaterialTheme.colorScheme.error,
            buttonTextColor = MaterialTheme.colorScheme.onError,
            buttonText = stringResource(R.string.DELETE_POET),
            onHeaderIconClick = onDismiss
        )
    }
}