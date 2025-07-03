package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    // FIXME does not handle scrolling very well when items are too many
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.neutralN70) },
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        content()
    }
}