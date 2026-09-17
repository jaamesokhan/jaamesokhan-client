package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
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
import ir.jaamebaade.jaamebaade_client.ui.theme.ButtonShape
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

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
                shape = ButtonShape
            ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        shape = ButtonShape,
        contentPadding = PaddingValues(horizontal = Dimens.space20, vertical = Dimens.space16)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
        )
    }
}