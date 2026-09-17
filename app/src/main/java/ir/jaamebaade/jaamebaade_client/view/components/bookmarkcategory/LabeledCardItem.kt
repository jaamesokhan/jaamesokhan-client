package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Label
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

/** Aligns the label row under CardItem's text column: avatar padding + avatar width + its spacer. */
private val labelRowStartIndent = Dimens.space20 + 66.dp + Dimens.space8

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LabeledCardItem(
    headerText: AnnotatedString,
    bodyText: String,
    imageUrl: String?,
    labels: List<Label>,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    onAddToCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CardItem(
            headerText = headerText,
            bodyText = bodyText,
            imageUrl = imageUrl,
            icon = Icons.Filled.MoreVert,
            onClick = onClick,
            onIconClick = onMoreClick,
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = labelRowStartIndent, end = Dimens.space20, bottom = Dimens.space12),
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
