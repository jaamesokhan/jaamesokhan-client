package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryText
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    headerText: String? = null,
    bodyText: AnnotatedString,
    footerText: String? = null,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClick: () -> Unit,
    onIconClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {

        if (headerText != null) {
            CardHeader(
                text = headerText
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = bodyText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                )
            }

            if (icon != null) {
                IconButton(
                    onClick = {
                        onIconClick()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = iconDescription)
                }
            }
        }
        if (footerText != null) {
            CardFooter(
                text = footerText
            )
        }
    }
}
@Composable
fun NewCardItem(
    modifier: Modifier = Modifier,
    headerText: String? = null,
    bodyText: AnnotatedString,
    footerText: String? = null,
    image: AsyncImagePainter,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClick: () -> Unit,
    onIconClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 10.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SquareImage(
                image = image,
                contentDescription = "",
                size = 70
            )
            // Texts columns
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 0.dp)
                    .weight(1f)
            ) {
                // Header and Icon Row
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    if (headerText != null) {
                        Text(
                            text = headerText,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (icon != null) {
                        IconButton(
                            onClick = {
                                onIconClick()
                            },
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = iconDescription)
                        }
                    }
                }

                Text(
                    modifier = Modifier.padding(2.dp),
                    text = bodyText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (footerText != null) {
                    Text(
                        text = footerText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,)
                }
            }
        }

    }

}