package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun HighlightActionButton(
    painter: Painter,
    text: String,
    outlined: Boolean = false,
    onClick: () -> Unit,
) {
    val buttonColors = if (outlined) {
        ButtonDefaults.outlinedButtonColors()
    } else {
        ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        )
    }

    val border = if (outlined) {
        BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        null
    }
    Button(
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp),
        colors = buttonColors,
        border = border,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = text,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
