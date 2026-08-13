package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.viewmodel.SelectionOptionViewModel

/** Word/phrase-meaning lookup for the current selection. Highlight/Copy are handled by
 * [SelectionToolbar] before this ever opens — this sheet is meaning-only, since a dictionary
 * entry needs more room than a floating toolbar can offer. */
@Composable
fun SelectionBottomSheet(
    viewModel: SelectionOptionViewModel,
    selectedText: String,
    changeMeaningFetchStatus: (status: Status) -> Unit,
    currentMeaningFetchStatus: Status,
    meaning: String,
    onDismiss: () -> Unit,
) {
    CustomBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        viewModel.getWordMeaning(
            word = selectedText,
            successCallBack = {
                changeMeaningFetchStatus(Status.SUCCESS)
            },
            failureCallBack = {
                changeMeaningFetchStatus(Status.FAILED)
            })
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    tint = MaterialTheme.colorScheme.secondaryS50,
                    contentDescription = null,
                    modifier = Modifier.size(8.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            when (currentMeaningFetchStatus) {
                Status.LOADING, Status.NOT_STARTED -> {
                    Box(modifier = Modifier.height(40.dp)) {
                        LoadingIndicator()
                    }
                }

                Status.FAILED -> {
                    Text("خطا در دریافت معنی")
                }

                Status.SUCCESS -> {
                    Text(
                        text = meaning,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier
                            .padding(
                                bottom = 32.dp
                            )
                            .verticalScroll(rememberScrollState()),
                    )
                }

                else -> {

                }
            }
        }
    }
}
