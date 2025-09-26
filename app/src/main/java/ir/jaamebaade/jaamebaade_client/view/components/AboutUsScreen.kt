package ir.jaamebaade.jaamebaade_client.view.components

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN100
import ir.jaamebaade.jaamebaade_client.view.components.base.RectangularButton

@Composable
fun AboutUsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val headlineTextStyle = MaterialTheme.typography.headlineMedium
    val bodyTextStyle = MaterialTheme.typography.bodyMedium
    Column(
        modifier = modifier
            .padding(start = 30.dp, end = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(76.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

            Icon(
                painter = painterResource(id = R.drawable.name_logo),
                modifier = Modifier
                    .size(width = 250.dp, height = 50.dp)
                    .scale(scaleX = -1f, scaleY = 1f),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.LOGO)
            )
        }
        Spacer(modifier = Modifier.height(76.dp))
        Text(text = stringResource(R.string.ABOUT_US_TITLE), style = headlineTextStyle)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.ABOUT_US_BASE),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.ABOUT_US_WEBSITE),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RectangularButton(
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.neutralN100,
                text = stringResource(R.string.WEBSITE),
                buttonHeight = 50.dp,
                buttonWidth = 248.dp,
                onClick = {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW, context.getString(R.string.WEBSITE_URL).toUri()
                    )
                    context.startActivity(urlIntent)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.ABOUT_US_GITHUB),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RectangularButton(
                backgroundColor = MaterialTheme.colorScheme.neutralN100,
                textColor = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.GITHUB),
                borderColor = MaterialTheme.colorScheme.primary,
                buttonHeight = 50.dp,
                buttonWidth = 248.dp,
                onClick = {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW, context.getString(R.string.GITHUB_URL).toUri()
                    )
                    context.startActivity(urlIntent)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.ABOUT_SUPPORT_TITLE), style = headlineTextStyle)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.COPY_RIGHT),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.ABOUT_US_SUPPORT),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RectangularButton(
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.neutralN100,
                text = stringResource(R.string.ABOUT_BE_SUPPORTIVE),
                buttonHeight = 50.dp,
                buttonWidth = 248.dp,
                onClick = {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW, context.getString(R.string.HAMIBASH_URL).toUri()
                    )
                    context.startActivity(urlIntent)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.ABOUT_CONTRIBUTION_TITLE), style = headlineTextStyle)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getString(R.string.ABOUT_US_EMAIL),
            style = bodyTextStyle,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RectangularButton(
                backgroundColor = MaterialTheme.colorScheme.neutralN100,
                textColor = MaterialTheme.colorScheme.primary,
                text = "Email",
                borderColor = MaterialTheme.colorScheme.primary,
                buttonHeight = 50.dp,
                buttonWidth = 248.dp,
                onClick = {
                    val emailIntent = Intent(
                        Intent.ACTION_SENDTO,
                        ("mailto:" + context.getString(R.string.EMAIL)).toUri()
                    )
                    context.startActivity(emailIntent)
                }
            )
        }
    }
}
