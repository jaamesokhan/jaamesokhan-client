package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.RandomPoemPreview
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton

@Composable
fun RandomPoemBox(
    randomPoemPreview: RandomPoemPreview,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(vertical = 32.dp)
            .height(228.dp)
            .fillMaxWidth(),
        onClick = {
            navController.navigate("${AppRoutes.POEM}/${randomPoemPreview.poemPath.poem.id}/${randomPoemPreview.poemPath.poem.id}/-1")
        }
    ) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                Column {
                    randomPoemPreview.verses.forEach {
                        Text(
                            text = it.text,
                            color = MaterialTheme.colorScheme.neutralN95,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SquareButton(
                        icon = Icons.Default.Autorenew,
                        tint = MaterialTheme.colorScheme.neutralN95,
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        text = null,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        roundedCornerShapeSize = 10,
                        size = 40,
                        onClick = { }
                    )
                    Text(
                        text = randomPoemPreview.poemPath.toPathHeaderText(),
                        color = MaterialTheme.colorScheme.neutralN95,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.random_poem),
                contentDescription = stringResource(R.string.RANDOM_POEM),
                modifier = Modifier.size(height = 228.dp, width = 110.dp)
            )
        }
    }
}