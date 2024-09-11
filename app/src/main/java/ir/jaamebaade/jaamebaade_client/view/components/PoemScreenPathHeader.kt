package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText

@Composable
fun PoemScreenPathHeader(
    navController: NavController,
    poetId: Int,
    poemId: Int,
    minId: Int,
    maxId: Int,
    path: VersePoemCategoriesPoet?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(4.dp),
    ) {

        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .size(32.dp),
            onClick = {
                navController.navigate(
                    "${AppRoutes.POEM}/${poetId}/${poemId - 1}/-1",
                    navOptions {
                        popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                            inclusive = true
                        }
                    }
                )
            },
            enabled = poemId - 1 >= minId
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.PREVIOUS)
            )
        }

        Text(
            modifier = Modifier.weight(0.8f),
            text = path?.toPathHeaderText(includePoemTitle = false) ?: "",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(
            modifier = Modifier
                .weight(0.1f)
                .size(32.dp),
            onClick = {
                navController.navigate(
                    "${AppRoutes.POEM}/${poetId}/${poemId + 1}/-1",
                    navOptions {
                        popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                            inclusive = true
                        }
                    }
                )
            },
            enabled = poemId + 1 <= maxId
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.NEXT),

                )
        }

    }
}