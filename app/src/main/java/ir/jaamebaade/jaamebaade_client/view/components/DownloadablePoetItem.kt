package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileDownloadOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.view.components.base.ComposableSquareButton
import ir.jaamebaade.jaamebaade_client.view.components.base.ComposableSquareButtonStyle
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage


@Composable
fun DownloadablePoetItem(
    poet: Poet,
    status: DownloadStatus,
    onButtonClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val iconSize = 18.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()

            },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            SquareImage(

                contentDescription = poet.name,
                imageUrl = poet.imageUrl,
                size = 66,
                roundedCornerShapeSize = 20,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text(
                    text = poet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground, //MaterialTheme.colorScheme.neutralN20,
                    maxLines = 3,
                    modifier = Modifier.weight(1f)
                )

                when (status) {
                    DownloadStatus.NotDownloaded -> {
                        ComposableSquareButton(
                            modifier =Modifier.height(50.dp)
                                .wrapContentWidth().widthIn(min = 116.dp),
                            style = ComposableSquareButtonStyle.Outlined,
                            onClick = onButtonClick
                        ) {
                            Icon(
                                modifier = Modifier.size(iconSize),
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.DOWNLOAD_POET)
                            )
                            Spacer(Modifier.width(1.dp))
                            Text(
                                stringResource(R.string.DOWNLOAD_POET),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }

                    DownloadStatus.Downloading -> {
                        ComposableSquareButton(
                            modifier = Modifier.height(50.dp)
                                .wrapContentWidth().widthIn(min = 116.dp) // default minimum width
                                 ,
                            style = ComposableSquareButtonStyle.Outlined,
                            onClick = {}
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(iconSize))

                        }
                    }

                    DownloadStatus.Downloaded -> {
                        ComposableSquareButton(
                            modifier = Modifier.height(50.dp)
                                .wrapContentWidth().widthIn(min = 116.dp) ,
                            style = ComposableSquareButtonStyle.Filled,
                            onClick = onButtonClick
                        ) {
                            Icon(
                                modifier = Modifier.size(iconSize),
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.OPEN_DOWNLOADED_POET_DESC)
                            )
                            Spacer(Modifier.width(1.dp))
                            Text(
                                stringResource(R.string.OPEN_DOWNLOADED_POET_DESC),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }

                    }

                    DownloadStatus.Failed -> {
                        ComposableSquareButton(
                            modifier = Modifier.height(50.dp)
                                .wrapContentWidth().widthIn(min = 116.dp) ,
                            style = ComposableSquareButtonStyle.Outlined,
                            onClick = onButtonClick
                        ) {
                            Icon(
                                modifier = Modifier.size(iconSize),
                                imageVector = Icons.Default.FileDownloadOff,
                                contentDescription = stringResource(R.string.DOWNLOAD_POET)
                            )
                            Spacer(Modifier.width(1.dp))
                            Text(
                                stringResource(R.string.DOWNLOAD_POET),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }

            }


        }
    }
}
