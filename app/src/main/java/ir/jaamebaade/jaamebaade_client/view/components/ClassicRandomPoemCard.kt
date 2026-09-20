package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95Light
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun ClassicRandomPoemCard(
    randomPoemPreview: RandomPoemPreview,
    onCardClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(228.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .fillMaxWidth(),
        onClick = onCardClick
    ) {

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(Dimens.space16)
                    .weight(1f)
                    .width(0.dp),
            ) {
                AnimatedContent(
                    targetState = randomPoemPreview,
                    modifier = Modifier.weight(1f),
                ) { targetRandomPoemPreview ->
                    Column {
                        Text(
                            text = targetRandomPoemPreview.verses.joinToString(separator = "\n") { it.text },
                            style = MaterialTheme.typography.bodyMedium,
                            color = neutralN95Light,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(0.4f),
                ) {
                    SquareButton(
                        icon = Icons.Default.Autorenew,
                        tint = neutralN95Light,
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        text = null,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        roundedCornerShapeSize = 10,
                        size = 32,
                        onClick = onRefreshClick
                    )
                    AnimatedContent(
                        targetState = randomPoemPreview,
                    ) { targetRandomPoemPreview ->
                        Text(
                            text = targetRandomPoemPreview.poemPath.toPathHeaderText(),
                            color = neutralN95Light,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.random_poem),
                contentDescription = stringResource(R.string.RANDOM_POEM),
                modifier = Modifier
                    .size(height = 228.dp, width = 110.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 320, name = "ClassicRandomPoemCard - light")
@Composable
private fun ClassicRandomPoemCardLightPreview() {
    RandomPoemPreviewFrame {
        ClassicRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 320, name = "ClassicRandomPoemCard - dark")
@Composable
private fun ClassicRandomPoemCardDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        ClassicRandomPoemCard(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
        )
    }
}
