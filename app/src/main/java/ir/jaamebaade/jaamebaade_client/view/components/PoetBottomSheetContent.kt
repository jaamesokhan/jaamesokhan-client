package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
import ir.jaamebaade.jaamebaade_client.view.components.base.ComposableSquareButton
import ir.jaamebaade.jaamebaade_client.view.components.base.RectangularButton
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage
import androidx.compose.material3.CircularProgressIndicator

enum class PoetInfoButtonType {
    ADD, OPEN, LOADING, FAILED, DELETE
}

@Composable
fun PoetBottomSheetContent(
    poet: Poet,
    showHeader: Boolean,
    showDescription: Boolean,
    headerText: String,
    buttonType: PoetInfoButtonType,
    onButtonClick: () -> Unit,
    buttonBackgroundColor: Color? = null,
    buttonTextColor: Color? = null,
    buttonText: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onButtonClick // This should be onDismiss, but for shared usage, caller should wrap
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.CLOSE),
                        tint = MaterialTheme.colorScheme.neutralN50
                    )
                }
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.neutralN50,
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                SquareImage(
                    imageUrl = poet.imageUrl ?: stringResource(R.string.FALLBACK_IMAGE_URL),
                    contentDescription = poet.name,
                    size = 89,
                )
                Text(
                    text = poet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp),
                )
                when (buttonType) {
                    PoetInfoButtonType.ADD -> {
                        RectangularButton(
                            backgroundColor = buttonBackgroundColor ?: MaterialTheme.colorScheme.surface,
                            textColor = buttonTextColor ?: MaterialTheme.colorScheme.primary,
                            borderColor = MaterialTheme.colorScheme.primary,
                            text = buttonText ?: stringResource(R.string.DOWNLOAD_POET),
                            onClick = onButtonClick,
                        )
                    }
                    PoetInfoButtonType.LOADING -> {
                        ComposableSquareButton(
                            style = ComposableSquareButtonStyle.Outlined,
                            onClick = onButtonClick,
                            buttonHeight = 50.dp,
                            buttonWidth = 114.dp
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp))
                        }
                    }
                    PoetInfoButtonType.DELETE -> {
                        RectangularButton(
                            backgroundColor = buttonBackgroundColor ?: MaterialTheme.colorScheme.error,
                            textColor = buttonTextColor ?: MaterialTheme.colorScheme.onError,
                            text = buttonText ?: stringResource(R.string.DELETE_POET),
                            onClick = onButtonClick,
                        )
                    }
                    else -> {
                        RectangularButton(
                            backgroundColor = buttonBackgroundColor ?: MaterialTheme.colorScheme.primary,
                            textColor = buttonTextColor ?: MaterialTheme.colorScheme.onPrimary,
                            text = buttonText ?: stringResource(R.string.OPEN_DOWNLOADED_POET_DESC),
                            onClick = onButtonClick,
                        )
                    }
                }
            }

            if (showDescription) {
                Text(
                    text = poet.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp),
                )
            }
        }
    }
}

