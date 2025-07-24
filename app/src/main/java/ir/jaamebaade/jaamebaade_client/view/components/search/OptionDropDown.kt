package ir.jaamebaade.jaamebaade_client.view.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet

data class DropDownToggleOption(
    val text: String,
    val key: Int?,
    var selected: MutableState<Boolean> = mutableStateOf(true),
)

@Composable
fun OptionDropDown(
    selectedText: String,
    opened: Boolean,
    onOpen: () -> Unit,
    onClose: () -> Unit,
    options: List<DropDownToggleOption>,
    allOptionsKey: Int?,
) {
    Box(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable { onOpen() }
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedText,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = stringResource(R.string.MORE)
            )
        }
        if (opened) {
            CustomBottomSheet(
                onDismissRequest = onClose
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = option.selected.value,
                            onCheckedChange = { isChecked ->
                                option.selected.value = isChecked

                                if (option.key == allOptionsKey) {
                                    options.forEach { it.selected.value = isChecked }
                                } else {
                                    if (!isChecked) {
                                        options.firstOrNull { it.key == allOptionsKey }?.selected?.value =
                                            false
                                    } else {
                                        val allSelected = options
                                            .filter { it.key != allOptionsKey }
                                            .all { it.selected.value }
                                        if (allSelected) {
                                            options.firstOrNull { it.key == allOptionsKey }?.selected?.value =
                                                true
                                        }
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option.text,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    if (option != options.last()) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
