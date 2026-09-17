package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.CATEGORY_COLOR_PALETTE
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun ColorSwatchPicker(
    selectedColor: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CATEGORY_COLOR_PALETTE.forEach { hex ->
            val isSelected = hex.equals(selectedColor, ignoreCase = true)
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(hex.toComposeColor())
                    .then(
                        if (isSelected) Modifier
                            .padding(Dimens.space2)
                            .border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        else Modifier
                    )
                    .clickable { onSelect(hex) }
            )
        }
    }
}
