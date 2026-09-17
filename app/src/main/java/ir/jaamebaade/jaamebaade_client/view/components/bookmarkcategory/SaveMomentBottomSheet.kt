package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.viewmodel.SaveMomentSheet
import ir.jaamebaade.jaamebaade_client.viewmodel.SaveMomentViewModel
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SaveMomentBottomSheet(
    bookmarkId: Int,
    onDismiss: () -> Unit,
    viewModel: SaveMomentViewModel = hiltViewModel(),
) {
    LaunchedEffect(bookmarkId) {
        viewModel.load()
    }

    if (viewModel.sheet == SaveMomentSheet.NEW_CATEGORY) {
        NewCategoryBottomSheet(
            isEditing = false,
            name = viewModel.draftName,
            onNameChange = viewModel::onDraftNameChange,
            color = viewModel.draftColor,
            onColorChange = viewModel::onDraftColorChange,
            onCancel = viewModel::cancelNewCategory,
            onSave = viewModel::createLabel,
        )
        return
    }

    CustomBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.SAVE_MOMENT_TITLE),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                viewModel.labels.forEach { label ->
                    CategoryToggleChip(
                        name = label.name,
                        color = label.color,
                        selected = label.id in viewModel.selected,
                        onClick = { viewModel.toggleSelection(label.id) },
                    )
                }
                AddCategoryChip(
                    text = stringResource(R.string.NEW_CATEGORY_CHIP),
                    onClick = viewModel::openNewCategory,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.NO_CATEGORY))
                }
                Button(
                    onClick = { viewModel.confirm(bookmarkId, onDismiss) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                ) {
                    Text(text = stringResource(R.string.SAVE))
                }
            }
        }
    }
}
