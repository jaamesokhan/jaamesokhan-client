package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

/** Text style for the verse or note shown in a [CollectionCardItem]. */
@Composable
fun collectionCardBodyStyle(): TextStyle =
    MaterialTheme.typography.headlineMedium.copy(fontSize = 17.sp, lineHeight = 32.sp)

/**
 * Card used by the saved, highlights and notes lists: a compact one-line header
 * (small poet avatar, poem path, more button) with the content laid out
 * full-width underneath it.
 */
@Composable
fun CollectionCardItem(
    pathText: String,
    imageUrl: String?,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space16),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(start = Dimens.space20, end = Dimens.space8, top = Dimens.space14, bottom = Dimens.space16),
            verticalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
            ) {
                SquareImage(
                    imageUrl = imageUrl,
                    contentDescription = null,
                    size = 44,
                    roundedCornerShapeSize = 20,
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = pathText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(modifier = Modifier.size(40.dp), onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.MORE),
                        tint = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = Dimens.space12),
                verticalArrangement = Arrangement.spacedBy(Dimens.space10),
                content = content,
            )
        }
    }
}
