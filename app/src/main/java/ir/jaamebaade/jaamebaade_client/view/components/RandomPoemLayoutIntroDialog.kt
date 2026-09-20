package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun RandomPoemLayoutIntroDialog(
    onChooseLayout: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        shape = MaterialTheme.shapes.extraLarge,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.RANDOM_POEM_LAYOUT_INTRO_TITLE)) },
        text = { Text(stringResource(R.string.RANDOM_POEM_LAYOUT_INTRO_BODY)) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                    onClick = onDismiss,
                ) {
                    ButtonLabel(stringResource(R.string.RANDOM_POEM_LAYOUT_INTRO_DISMISS))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                    onClick = onChooseLayout,
                ) {
                    ButtonLabel(stringResource(R.string.RANDOM_POEM_LAYOUT_INTRO_CONFIRM))
                }
            }
        },
    )
}

@Composable
private fun ButtonLabel(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.copy(
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both,
            ),
        ),
    )
}

@Preview(showBackground = true, widthDp = 428, heightDp = 380, name = "RandomPoemLayoutIntroDialog - light")
@Composable
private fun RandomPoemLayoutIntroDialogLightPreview() {
    RandomPoemPreviewFrame {
        RandomPoemLayoutIntroDialog(onChooseLayout = {}, onDismiss = {})
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 380, name = "RandomPoemLayoutIntroDialog - dark")
@Composable
private fun RandomPoemLayoutIntroDialogDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        RandomPoemLayoutIntroDialog(onChooseLayout = {}, onDismiss = {})
    }
}
