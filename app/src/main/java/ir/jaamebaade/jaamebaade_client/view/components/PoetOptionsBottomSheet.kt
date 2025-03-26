package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70
import ir.jaamebaade.jaamebaade_client.view.components.base.RectangularButton
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetOptionsBottomSheet(
    poet: Poet,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    showHeader: Boolean = true,
    showDescription: Boolean = true,
    onDeleteClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                poet.imageUrl ?: "https://ganjoor.net/image/gdap.png"
            )
            .size(coil.size.Size.ORIGINAL)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.neutralN70) },
        containerColor = if (showHeader) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss, sheetState = sheetState
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
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.CLOSE),
                            tint = MaterialTheme.colorScheme.neutralN50
                        )
                    }
                    Text(
                        text = stringResource(R.string.DELETE_POET),
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
                        image = painter,
                        contentDescription = poet.name,
                        size = 89,
                    )
                    Text(
                        text = poet.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp),
                    )
                    RectangularButton(
                        backgroundColor = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.onError,
                        text = stringResource(R.string.DELETE_POET),
                        onClick = onDeleteClick,
                    )
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
}
