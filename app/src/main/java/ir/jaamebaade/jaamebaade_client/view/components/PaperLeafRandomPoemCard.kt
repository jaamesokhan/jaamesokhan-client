package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

private const val AVATAR_SIZE = 30

private const val VERSE_LINES = 2

@Composable
fun PaperLeafRandomPoemCard(
    randomPoemPreview: RandomPoemPreview,
    onCardClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = randomPoemAccentColor()
    val hairline = MaterialTheme.colorScheme.outline
    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        border = BorderStroke(1.dp, hairline),
    ) {
        Column(
            modifier = Modifier.padding(
                start = Dimens.space20,
                end = Dimens.space20,
                top = Dimens.space16,
                bottom = Dimens.space4,
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
            ) {
                AnimatedContent(
                    targetState = randomPoemPreview,
                    modifier = Modifier.weight(1f),
                ) { preview ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
                    ) {
                        PoetAvatar(poet = preview.poemPath.poet, accent = accent)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preview.poemPath.poet.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = preview.poemPath.categories.joinToString(" · ") { it.text },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.RANDOM_POEM),
                    style = MaterialTheme.typography.labelSmall,
                    color = accent,
                    modifier = Modifier
                        .border(BorderStroke(1.dp, hairline), CircleShape)
                        .padding(horizontal = Dimens.space10, vertical = Dimens.space4),
                )
            }

            AnimatedContent(targetState = randomPoemPreview) { preview ->
                Text(
                    text = preview.verses.take(VERSE_LINES).joinToString(separator = "\n") { it.text },
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 32.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            HorizontalDivider(color = hairline)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onRefreshClick)
                .heightIn(min = 44.dp)
                .padding(horizontal = Dimens.space20),
        ) {
            Icon(
                imageVector = Icons.Default.Autorenew,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = stringResource(R.string.RANDOM_POEM_ANOTHER),
                style = MaterialTheme.typography.labelMedium,
                color = accent,
            )
        }
    }
}

@Composable
private fun PoetAvatar(poet: Poet, accent: Color) {
    if (poet.imageUrl.isNullOrEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(AVATAR_SIZE.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.15f)),
        ) {
            Text(
                text = poet.name.take(1),
                style = MaterialTheme.typography.titleSmall,
                color = accent,
            )
        }
    } else {
        SquareImage(
            imageUrl = poet.imageUrl,
            contentDescription = poet.name,
            size = AVATAR_SIZE,
            scale = 1f,
            roundedCornerShapeSize = AVATAR_SIZE / 2,
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 240, name = "PaperLeafRandomPoemCard - light")
@Composable
private fun PaperLeafRandomPoemCardLightPreview() {
    RandomPoemPreviewFrame {
        PaperLeafRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 240, name = "PaperLeafRandomPoemCard - dark")
@Composable
private fun PaperLeafRandomPoemCardDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        PaperLeafRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}
