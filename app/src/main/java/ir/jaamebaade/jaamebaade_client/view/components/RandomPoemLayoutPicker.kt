package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.RandomPoemLayoutType

@Composable
fun RandomPoemLayoutPicker(
    selected: RandomPoemLayoutType,
    samplePoem: RandomPoemPreview,
    onSelect: (RandomPoemLayoutType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .selectableGroup()
            .padding(horizontal = Dimens.space16)
            .padding(bottom = Dimens.space24),
        verticalArrangement = Arrangement.spacedBy(Dimens.space16),
    ) {
        RandomPoemLayoutType.entries.forEach { layout ->
            val isSelected = layout == selected
            val shape = RoundedCornerShape(Dimens.space16)
            if (layout == RandomPoemLayoutType.HIDDEN) {
                Text(
                    text = layout.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .selectable(
                            selected = isSelected,
                            role = Role.RadioButton,
                            onClick = { onSelect(layout) },
                        )
                        .border(
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                            ),
                            shape = shape,
                        )
                        .padding(Dimens.space16),
                )
                return@forEach
            }
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                Text(
                    text = layout.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .border(
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                            ),
                            shape = shape,
                        )
                        .padding(Dimens.space8),
                ) {
                    RandomPoemCard(
                        randomPoemPreview = samplePoem,
                        onCardClick = {},
                        onRefreshClick = {},
                        layout = layout,
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { onSelect(layout) },
                            ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 900, name = "RandomPoemLayoutPicker - light")
@Composable
private fun RandomPoemLayoutPickerLightPreview() {
    RandomPoemPreviewFrame {
        RandomPoemLayoutPicker(
            selected = RandomPoemLayoutType.PAPER_LEAF,
            samplePoem = rememberSampleRandomPoemPreview(),
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 900, name = "RandomPoemLayoutPicker - hidden selected")
@Composable
private fun RandomPoemLayoutPickerHiddenPreview() {
    RandomPoemPreviewFrame {
        RandomPoemLayoutPicker(
            selected = RandomPoemLayoutType.HIDDEN,
            samplePoem = rememberSampleRandomPoemPreview(),
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 900, name = "RandomPoemLayoutPicker - dark")
@Composable
private fun RandomPoemLayoutPickerDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        RandomPoemLayoutPicker(
            selected = RandomPoemLayoutType.JADVAL_LEAF,
            samplePoem = rememberSampleRandomPoemPreview(),
            onSelect = {},
        )
    }
}
