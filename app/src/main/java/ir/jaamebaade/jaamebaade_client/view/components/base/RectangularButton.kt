package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RectangularButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    text: String,
    buttonWidth: Dp = Dp.Unspecified,
    buttonHeight: Dp = Dp.Unspecified,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 1.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .then(
                if (buttonWidth != Dp.Unspecified && buttonHeight != Dp.Unspecified) {
                    Modifier.size(width = buttonWidth, height = buttonHeight)
                } else Modifier
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(15.dp)
            ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(
            modifier = modifier.padding(horizontal = 32.dp, vertical = 2.dp),
            text = text,
            style = textStyle,
            color = textColor,
        )
    }
}