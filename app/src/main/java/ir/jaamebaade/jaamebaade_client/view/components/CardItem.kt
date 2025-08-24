package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.CommentPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordPathFirstVerse
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage
import java.util.Date

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
                imageUrl = imageUrl ?: stringResource(R.string.FALLBACK_IMAGE_URL),
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
                            text = it,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = if (wrapHeader) Int.MAX_VALUE else 1,
                            overflow = if (wrapHeader) TextOverflow.Clip else TextOverflow.Ellipsis
                        )
                    }

                    icon?.let {
                        IconButton(
                            modifier = Modifier.padding(end = 5.dp),
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
fun HighlightCardItem(
    modifier: Modifier = Modifier,
    headerText: AnnotatedString? = null,
    bodyText: String,
    imageUrl: String?,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onClick: () -> Unit,
    onIconClick: () -> Unit = {},
) {
    NewCardItem(
        modifier = modifier,
        headerText = headerText,
        bodyText = bodyText,
        imageUrl = imageUrl,
        icon = icon,
        iconDescription = iconDescription,
        onClick = onClick,
        onIconClick = onIconClick,
        wrapHeader = true,
        wrapBody = true,
    )

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
        ) {
            SquareImage(
                imageUrl = imageUrl ?: stringResource(R.string.FALLBACK_IMAGE_URL),
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
                    modifier = Modifier.fillMaxWidth().padding(end =  24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        header?.let { it() }
                        body()
                    }
                    if (icon != null) {
                        IconButton(
                            modifier = Modifier.padding(end = 5.dp),
                            onClick = onIconClick
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = iconDescription,
                            )
                        }
                    }

                }

                footer?.let { it() }
            }
        }
    }
}

@Composable
fun MyNoteCardItem(
    modifier: Modifier = Modifier,
    note: CommentPoemCategoriesPoet,
    onClick: () -> Unit = {},
    onIconClick: () -> Unit = {},
) {
    ComposableCardItem(
        modifier = modifier,
        imageUrl = note.path.poet.imageUrl,
        header = {
            Text(
                text = note.path.poem.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        body = {
            Text(
                text = note.comment.text.trim(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        },
        footer =
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        tint = MaterialTheme.colorScheme.secondaryS50,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Date(note.comment.createdAt).convertToJalali()
                            .toLocalFormatWithHour(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

            },
        icon = Icons.Filled.MoreVert,
        iconDescription = stringResource(R.string.MORE),
        onClick = onClick,
        onIconClick = onIconClick
    )
}


@Composable
fun MyHistoryCardItem(
    modifier: Modifier = Modifier,
    historyRecord: HistoryRecordPathFirstVerse,
    onClick: () -> Unit = {},
) {
    ComposableCardItem(
        modifier = modifier,
        imageUrl = historyRecord.path.poet.imageUrl,
        header = {
            Text(
                text = historyRecord.path.poem.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        body = {
            Text(
                text = historyRecord.firstVerse.text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.outlineVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        footer =
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        tint = MaterialTheme.colorScheme.secondaryS50,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Date(historyRecord.timestamp).convertToJalali()
                            .toLocalFormatWithHour(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

            },
        icon = null,
        iconDescription = null,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.background
    )
}