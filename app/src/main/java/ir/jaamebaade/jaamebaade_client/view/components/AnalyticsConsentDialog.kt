package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun AnalyticsConsentDialog(
    onAllow: () -> Unit,
    onDeny: () -> Unit,
) {
    AlertDialog(
        shape = MaterialTheme.shapes.extraLarge,
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = { Text(stringResource(R.string.ANALYTICS_CONSENT_TITLE)) },
        text = { Text(stringResource(R.string.ANALYTICS_CONSENT_BODY)) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                    onClick = onDeny,
                ) {
                    Text(stringResource(R.string.ANALYTICS_CONSENT_DENY), textAlign = TextAlign.Center)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                    onClick = onAllow,
                ) {
                    Text(stringResource(R.string.ANALYTICS_CONSENT_ALLOW), textAlign = TextAlign.Center)
                }
            }
        },
    )
}

@Preview(showBackground = true, widthDp = 428, heightDp = 380, name = "AnalyticsConsentDialog - light")
@Composable
private fun AnalyticsConsentDialogLightPreview() {
    RandomPoemPreviewFrame {
        AnalyticsConsentDialog(onAllow = {}, onDeny = {})
    }
}

@Preview(showBackground = true, widthDp = 428, heightDp = 380, name = "AnalyticsConsentDialog - dark")
@Composable
private fun AnalyticsConsentDialogDarkPreview() {
    RandomPoemPreviewFrame(darkTheme = true) {
        AnalyticsConsentDialog(onAllow = {}, onDeny = {})
    }
}
