package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    headerText: AnnotatedString? = null,
    bodyText: String,
    footerText: String? = null,
    imageUrl: String?,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClick: () -> Unit,
    onIconClick: () -> Unit = {},
    wrapHeader: Boolean = false,
    wrapBody: Boolean = false,
    wrapFooter: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            SquareImage(
                imageUrl = imageUrl,
                contentDescription = null,
                size = 66,
                roundedCornerShapeSize = 20,
                modifier = Modifier.padding(start = 20.dp, end = 0.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    headerText?.let {
                        Text(
                            modifier = Modifier.weight(7f),
                            text = it,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = if (wrapHeader) Int.MAX_VALUE else 3,
                            overflow = if (wrapHeader) TextOverflow.Clip else TextOverflow.Ellipsis
                        )
                    }

                    icon?.let {
                        IconButton(
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .weight(1f),
                            onClick = onIconClick
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = iconDescription,
                            )
                        }
                    }
                }

                // Body
                Text(
                    text = bodyText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    maxLines = if (wrapBody) Int.MAX_VALUE else 1,
                    overflow = if (wrapBody) TextOverflow.Clip else TextOverflow.Ellipsis
                )

                // Footer
                footerText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        maxLines = if (wrapFooter) Int.MAX_VALUE else 1,
                        overflow = if (wrapFooter) TextOverflow.Clip else TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
fun ComposableCardItem(
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
    footer: (@Composable () -> Unit)? = null,
    imageUrl: String? = null,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClick: () -> Unit,
    onIconClick: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background, // Default background color

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            SquareImage(
                imageUrl = imageUrl,
                contentDescription = null,
                size = 66,
                roundedCornerShapeSize = 20,
                modifier = Modifier.padding(start = 20.dp, end = 0.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                       // .padding(end = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(7f)
                    ) {
                        //Column() {
                            header?.let { it() }


                        //}
                    }
                    if (icon != null) {
                        IconButton(
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .weight(1f),
                            onClick = onIconClick
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = iconDescription,
                            )
                        }
                    }

                }
                body()
                footer?.let { it() }
            }
        }
    }
}
