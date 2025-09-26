package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    onDismissRequest: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.neutralN70) },
        containerColor = containerColor,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        content()
    }
}
