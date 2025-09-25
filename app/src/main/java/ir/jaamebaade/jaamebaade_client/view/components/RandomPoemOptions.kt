package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode
import java.time.LocalTime
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RandomPoemOptions(
    modifier: Modifier = Modifier,
    downloadedPoetViewModel: MyPoetsViewModel = hiltViewModel()
) {
    //TODO must be redesigned
    val poetsWithCategories = downloadedPoetViewModel.categories
    val isDailyRandomPoemEnabled = downloadedPoetViewModel.isScheduledNotificationsEnabled
    val scheduledNotificationTime = downloadedPoetViewModel.scheduledNotificationTime
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }

    val formattedTime = String.format(
        Locale.getDefault(),
        "%02d:%02d",
        scheduledNotificationTime.hour,
        scheduledNotificationTime.minute
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {


        // Title
        Text(
            text = stringResource(id = R.string.RANDOM_POEM_OPTIONS),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.DAILY_RANDOM_POEM_NOTIFICATION),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDailyRandomPoemEnabled,
                onCheckedChange = {
                    downloadedPoetViewModel.setScheduledNotificationsEnabled(context, it)
                }
            )
        }

        AnimatedVisibility(visible = isDailyRandomPoemEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.DAILY_RANDOM_POEM_TIME_LABEL,
                        formattedTime
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                OutlinedButton(onClick = { showTimePicker = true }) {
                    Text(text = stringResource(id = R.string.CHANGE_TIME))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category checklist
        poetsWithCategories?.let { categories ->
            RecursiveCheckList(categories = categories, onChange = {
                downloadedPoetViewModel.saveSelectedCategoriesForRandomPoem()
            })
        }

        if (showTimePicker) {
            val pickerState = rememberTimePickerState(
                initialHour = scheduledNotificationTime.hour,
                initialMinute = scheduledNotificationTime.minute,
                is24Hour = true
            )

            BasicAlertDialog(onDismissRequest = { showTimePicker = false }) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.CHANGE_TIME),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        TimePicker(
                            state = pickerState,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showTimePicker = false }) {
                                Text(text = stringResource(id = R.string.CANCEL))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                downloadedPoetViewModel.updateScheduledNotificationTime(
                                    context,
                                    LocalTime.of(pickerState.hour, pickerState.minute)
                                )
                                showTimePicker = false
                            }) {
                                Text(text = stringResource(id = R.string.SAVE))
                            }
                        }
                    }
                }
            }
        }

    }


}

@Composable
private fun RecursiveCheckList(
    categories: List<CategoryGraphNode>,
    onChange: () -> Unit
) {
    categories.forEach { category ->
        val canExpand = category.subCategories.isNotEmpty()
        var selectedForRandomState by remember { category.selectedForRandomState }

        ExpandableCheckbox(
            state = selectedForRandomState,
            onCheckedChange = {
                selectedForRandomState = if (selectedForRandomState == ToggleableState.On) {
                    ToggleableState.Off
                } else {
                    ToggleableState.On
                }
                changeCheckRecursively(category, selectedForRandomState)
                updateParentCheckStatus(category)
                onChange()
            },
            text = category.category.text,
            canExpand = canExpand
        ) {
            if (canExpand) {
                RecursiveCheckList(categories = category.subCategories, onChange = onChange)
            }
        }
    }
}

private fun changeCheckRecursively(
    category: CategoryGraphNode,
    selectedState: ToggleableState
) {
    category.selectedForRandomState.value = selectedState
    category.subCategories.forEach { subCategory ->
        changeCheckRecursively(subCategory, selectedState)
    }
}

private fun updateParentCheckStatus(category: CategoryGraphNode) {
    val parent = category.parent ?: return
    val allSelected =
        parent.subCategories.all { it.selectedForRandomState.value == ToggleableState.On }
    val noneSelected =
        parent.subCategories.all { it.selectedForRandomState.value == ToggleableState.Off }
    if (allSelected) {
        parent.selectedForRandomState.value = ToggleableState.On
    } else if (noneSelected) {
        parent.selectedForRandomState.value = ToggleableState.Off
    } else {
        parent.selectedForRandomState.value = ToggleableState.Indeterminate
    }
    updateParentCheckStatus(parent)
}
