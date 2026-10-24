package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage
import ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory.dashedBorder

private const val AVATAR_SIZE = 72

@Composable
fun PoetIconButton(poet: Poet, onLongClick: () -> Unit, onClick: () -> Unit) {
    val enabled = poet.downloadStatus != DownloadStatus.Downloading
    PoetTile(
        modifier = Modifier.alpha(if (enabled) 1f else 0.4f),
        label = poet.name,
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClick,
        avatar = {
            SquareImage(
                imageUrl = poet.imageUrl,
                contentDescription = poet.name,
                size = AVATAR_SIZE,
                roundedCornerShapeSize = AVATAR_SIZE / 2,
            )
        },
        overlay = {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp),
                enabled = enabled,
                onClick = onLongClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.MORE),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        },
    )
}

@Composable
fun AddPoetTile(onClick: () -> Unit) {
    PoetTile(
        label = stringResource(R.string.ADD_NEW_POET),
        labelWeight = FontWeight.Normal,
        onClick = onClick,
        avatar = {
            Box(
                modifier = Modifier
                    .size(AVATAR_SIZE.dp)
                    .dashedBorder(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        cornerRadius = (AVATAR_SIZE / 2).dp,
                        strokeWidth = 2.dp,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        },
    )
}

/** Rounded box holding a poet avatar with its name underneath, as on the PWA home grid. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PoetTile(
    label: String,
    onClick: () -> Unit,
    avatar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    labelWeight: FontWeight = FontWeight.Bold,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(enabled = enabled, onClick = onClick, onLongClick = onLongClick)
                .padding(start = Dimens.space4, end = Dimens.space4, top = Dimens.space16, bottom = Dimens.space12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.space8),
        ) {
            avatar()
            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = labelWeight,
                color = if (labelWeight == FontWeight.Bold) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                textAlign = TextAlign.Center,
            )
        }
        overlay()
    }
}
