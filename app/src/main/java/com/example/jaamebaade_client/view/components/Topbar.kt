package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jaamebaade_client.R
import com.example.jaamebaade_client.constants.AppRoutes
import com.example.jaamebaade_client.viewmodel.TopBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, viewModel: TopBarViewModel = hiltViewModel()) {
    val myIcon = painterResource(id = R.mipmap.logo)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canPop =
        (backStackEntry?.destination?.route != AppRoutes.DOWNLOADED_POETS_SCREEN.toString()
                && backStackEntry?.destination?.route != AppRoutes.SETTINGS_SCREEN.toString()
                && backStackEntry?.destination?.route != AppRoutes.SEARCH_SCREEN.toString()
                && backStackEntry?.destination?.route != AppRoutes.FAVORITE_SCREEN.toString())
    val breadCrumbs = viewModel.breadCrumbs

    LaunchedEffect(key1 = backStackEntry) {
        viewModel.updateBreadCrumbs(backStackEntry)
    }
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        myIcon,
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = breadCrumbs,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.sizeIn(
                            maxWidth = 250.dp
                        ),
                        maxLines = 1
                    )
                }

                if (canPop) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable {
                                if (backStackEntry?.destination?.route == AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                                    navController.navigate(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                                        popUpTo(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                                            inclusive = true
                                        }

                                    }
                                } else {
                                    navController.popBackStack()
                                }
                            }
                            .padding(end = 16.dp)
                            .requiredWidth(24.dp)
                            .size(24.dp),
                    )
                }
            }
        },
    )
}
