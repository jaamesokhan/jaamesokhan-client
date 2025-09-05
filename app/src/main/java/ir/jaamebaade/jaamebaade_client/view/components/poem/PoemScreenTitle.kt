package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage

@Composable
fun PoemScreenTitle(
    navController: NavController,
    poemPath: VersePoemCategoriesPoet,
    minId: Int,
    maxId: Int,
) {
    val poetId = poemPath.poet.id
    val poemId = poemPath.poem.id

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SquareImage(
            modifier = Modifier.padding(bottom = 8.dp),
            imageUrl = poemPath.poet.imageUrl,
            contentDescription = poemPath.poet.name,
            size = 88,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 10.dp),
        ) {
            IconButton(
                modifier = Modifier
                    .weight(0.1f)
                    .size(32.dp), onClick = {
                    navController.navigate(
                        "${AppRoutes.POEM}/${poetId}/${poemId - 1}/-1", navOptions {
                            popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                                inclusive = true
                            }
                        })
                }, enabled = (poemId - 1) >= minId

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.PREVIOUS),
                    tint = if ((poemId - 1) < minId) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        0.1f
                    ) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            Text(
                modifier = Modifier.weight(0.8f),
                text = poemPath.poem.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                modifier = Modifier
                    .weight(0.1f)
                    .size(32.dp), onClick = {
                    navController.navigate(
                        "${AppRoutes.POEM}/${poetId}/${poemId + 1}/-1", navOptions {
                            popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                                inclusive = true
                            }
                        })
                }, enabled = (poemId + 1) <= maxId
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.NEXT),
                    tint = if ((poemId + 1) > maxId) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        0.1f
                    ) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
    }
}