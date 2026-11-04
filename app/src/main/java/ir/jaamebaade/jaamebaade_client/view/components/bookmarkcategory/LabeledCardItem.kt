package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.view.components.CollectionCardItem
import ir.jaamebaade.jaamebaade_client.view.components.collectionCardBodyStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LabeledCardItem(
    pathText: String,
    bodyText: AnnotatedString,
    imageUrl: String?,
    labels: List<Label>,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    onAddToCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
    bodyMaxLines: Int = Int.MAX_VALUE,
) {
    CollectionCardItem(
        modifier = modifier,
        pathText = pathText,
        imageUrl = imageUrl,
        onClick = onClick,
        onMoreClick = onMoreClick,
    ) {
        Text(
            text = bodyText,
            style = collectionCardBodyStyle(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = bodyMaxLines,
            overflow = TextOverflow.Ellipsis,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            labels.forEach { label ->
                CategoryTagPill(name = label.name, color = label.color)
            }
            if (labels.isEmpty()) {
                AddToCategoryPill(
                    text = stringResource(R.string.ADD_TO_CATEGORY_PILL),
                    onClick = onAddToCategoryClick,
                )
            }
        }
    }
}
