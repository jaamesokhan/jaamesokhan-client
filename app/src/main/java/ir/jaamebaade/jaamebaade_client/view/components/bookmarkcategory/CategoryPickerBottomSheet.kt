package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.LabelWithCount
import ir.jaamebaade.jaamebaade_client.ui.theme.ButtonShape
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun CategoryPickerBottomSheet(
    labels: List<LabelWithCount>,
    selected: Set<Int>,
    onToggle: (Int) -> Unit,
    onCreateNew: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    CustomBottomSheet(onDismissRequest = onCancel) {
        Column {
            Column(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                labels.forEach { labelWithCount ->
                    val isChecked = labelWithCount.label.id in selected
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(labelWithCount.label.id) }
                            .padding(horizontal = Dimens.space24, vertical = Dimens.space8),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onToggle(labelWithCount.label.id) },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.tertiary)
                        )
                        ColorDot(color = labelWithCount.label.color)
                        Text(
                            text = labelWithCount.label.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = labelWithCount.itemCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onCreateNew)
                        .padding(horizontal = Dimens.space24, vertical = Dimens.space16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = stringResource(R.string.NEW_CATEGORY),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.space24, vertical = Dimens.space16),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = ButtonShape,
                ) {
                    Text(text = stringResource(R.string.CANCEL))
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = ButtonShape,
                ) {
                    Text(text = stringResource(R.string.CONFIRM))
                }
            }
        }
    }
}
