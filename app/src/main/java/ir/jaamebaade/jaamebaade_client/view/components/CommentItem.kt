package ir.jaamebaade.jaamebaade_client.view.components

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.poem.MenuRowItem
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentItem(modifier: Modifier = Modifier, comment: Comment, onShareClicked: () -> Unit, onDeleteClicked: () -> Unit) {
    var showOptions by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(0.9f)) {
                    Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .weight(0.1f)
                ) {
                    IconButton(
                        onClick = {
                            showOptions = !showOptions
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    tint = MaterialTheme.colorScheme.secondaryS50,
                    contentDescription = null,
                    modifier = Modifier.size(8.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Date(comment.createdAt).convertToJalali().toLocalFormatWithHour(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }

            if (showOptions) {
                CustomBottomSheet({ showOptions = false }) {
                    Column {
                        MenuRowItem(
                            activatedIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = null
                                )
                            },
                            deactivatedText = stringResource(R.string.SHARE),
                        ) {
                            onShareClicked()
                            showOptions = false
                        }
                        MenuRowItem(
                            activatedIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            },
                            deactivatedText = stringResource(R.string.DELETE),
                            textColor = MaterialTheme.colorScheme.error,
                            includeBottomLine = false,
                        ) {
                            onDeleteClicked()
                            showOptions = false
                        }
                    }
                }
            }
        }
    }
}
