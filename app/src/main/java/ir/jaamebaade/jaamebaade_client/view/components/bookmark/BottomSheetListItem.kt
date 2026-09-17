package ir.jaamebaade.jaamebaade_client.view.components.bookmark

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun BottomSheetListItem(
    icon: ImageVector,
    text: String,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick:  () -> Unit
) {
    Surface(
        onClick = onClick,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.space28, vertical = Dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(Dimens.space10))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge
            )

        }
    }
}
