package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun RectangularButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
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