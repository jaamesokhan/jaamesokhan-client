package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

private val JadvalCream = Color(0xFFFBF7EC)
private val JadvalCreamInk = Color(0xFF2B2A21)
private val JadvalCreamHairline = Color(0xFFD8D2BC)

private const val VERSE_LINES = 2

@Composable
fun JadvalLeafRandomPoemCard(
    randomPoemPreview: RandomPoemPreview,
    onCardClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val accent = randomPoemAccentColor()
    val paper = if (isDark) MaterialTheme.colorScheme.surfaceContainer else JadvalCream
    val ink = if (isDark) MaterialTheme.colorScheme.onSurface else JadvalCreamInk
    val cartoucheHairline = if (isDark) MaterialTheme.colorScheme.outline else JadvalCreamHairline
    val nastaliq = CustomFonts.Nastaliq

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(paper)
            .clickable(onClick = onCardClick)
            .padding(Dimens.space6)
            .border(1.dp, accent.copy(alpha = 0.5f))
            .padding(Dimens.space2)
            .border(3.dp, accent)
            .padding(
                start = Dimens.space20,
                end = Dimens.space20,
                top = Dimens.space16,
                bottom = Dimens.space4,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        AnimatedContent(targetState = randomPoemPreview) { preview ->
            Text(
                text = preview.verses.take(VERSE_LINES).joinToString(separator = "\n") { it.text },
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = nastaliq.fontFamily,
                    fontSize = nastaliq.specs.body.large.fontSize,
                    lineHeight = 40.sp,
                ),
                color = ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        AnimatedContent(targetState = randomPoemPreview) { preview ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
                modifier = Modifier.fillMaxWidth(),
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = cartoucheHairline)
                Text(
                    text = listOfNotNull(
                        preview.poemPath.poet.name,
                        preview.poemPath.categories.lastOrNull()?.text,
                    ).joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = accent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = cartoucheHairline)
            }
        }

        IconButton(onClick = onRefreshClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Default.Autorenew,
                contentDescription = stringResource(R.string.RANDOM_POEM),
                tint = accent,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 240, name = "JadvalLeafRandomPoemCard - light")
@Composable
private fun JadvalLeafRandomPoemCardLightPreview() {
    RandomPoemPreviewFrame {
        JadvalLeafRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 240, name = "JadvalLeafRandomPoemCard - dark")
@Composable
private fun JadvalLeafRandomPoemCardDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        JadvalLeafRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}
