package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

/**
 * Hairline divider under a list row with a leading avatar/thumbnail.
 * [startIndent] should match the row's leading-content width (avatar +
 * its own padding) so the rule starts after it instead of under it.
 */
@Composable
fun ListRowDivider(startIndent: Dp = 90.dp) {
    HorizontalDivider(
        modifier = Modifier.padding(
            start = startIndent,
            top = Dimens.space6,
            bottom = Dimens.space6
        ),
        color = MaterialTheme.colorScheme.outline
    )
}
