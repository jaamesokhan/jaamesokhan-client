package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.Radius
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton

@Composable
fun PoetIconButton(poet: Poet, onLongClick: () -> Unit, onClick: () -> Unit) {
    SquareButton(
        modifier = Modifier.padding(bottom = Dimens.space16),
        imageUrl = poet.imageUrl,
        tint = Color.White,
        contentDescription = poet.name,
        textStyle = MaterialTheme.typography.headlineSmall,
        backgroundColor = Color.Transparent,
        onClick = onClick,
        onLongClick = onLongClick,
        roundedCornerShapeSize = Radius.card.value.toInt(),
        iconSize = 65,
        size = 70
    )
}