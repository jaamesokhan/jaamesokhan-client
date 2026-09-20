package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.RandomPoemLayoutType

@Composable
fun RandomPoemBox(
    randomPoemPreview: RandomPoemPreview,
    layout: RandomPoemLayoutType,
    onCardClick: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    RandomPoemCard(
        randomPoemPreview = randomPoemPreview,
        onCardClick = onCardClick,
        onRefreshClick = onRefreshClick,
        layout = layout,
        modifier = Modifier.padding(vertical = Dimens.space34),
    )
}

@Composable
fun RandomPoemCard(
    randomPoemPreview: RandomPoemPreview,
    onCardClick: () -> Unit,
    onRefreshClick: () -> Unit,
    layout: RandomPoemLayoutType,
    modifier: Modifier = Modifier,
) {
    when (layout) {
        RandomPoemLayoutType.CLASSIC -> ClassicRandomPoemCard(
            randomPoemPreview = randomPoemPreview,
            onCardClick = onCardClick,
            onRefreshClick = onRefreshClick,
            modifier = modifier,
        )

        RandomPoemLayoutType.PAPER_LEAF -> PaperLeafRandomPoemCard(
            randomPoemPreview = randomPoemPreview,
            onCardClick = onCardClick,
            onRefreshClick = onRefreshClick,
            modifier = modifier,
        )

        RandomPoemLayoutType.JADVAL_LEAF -> JadvalLeafRandomPoemCard(
            randomPoemPreview = randomPoemPreview,
            onCardClick = onCardClick,
            onRefreshClick = onRefreshClick,
            modifier = modifier,
        )

        RandomPoemLayoutType.HIDDEN -> Unit
    }
}

@Composable
internal fun randomPoemAccentColor(): Color {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    return if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
}

@Preview(showBackground = true, widthDp = 428, heightDp = 340, name = "RandomPoemBox - classic")
@Composable
private fun RandomPoemBoxClassicPreview() {
    RandomPoemPreviewFrame {
        RandomPoemBox(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
            layout = RandomPoemLayoutType.CLASSIC,
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 340, name = "RandomPoemBox - paper leaf")
@Composable
private fun RandomPoemBoxPaperLeafPreview() {
    RandomPoemPreviewFrame {
        RandomPoemBox(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
            layout = RandomPoemLayoutType.PAPER_LEAF,
        )
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 340, name = "RandomPoemBox - jadval leaf")
@Composable
private fun RandomPoemBoxJadvalLeafPreview() {
    RandomPoemPreviewFrame {
        RandomPoemBox(
            randomPoemPreview = rememberSampleRandomPoemPreview(),
            onCardClick = {},
            onRefreshClick = {},
            layout = RandomPoemLayoutType.JADVAL_LEAF,
        )
    }
}
