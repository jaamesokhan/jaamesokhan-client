package com.example.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jaamebaade_client.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun TopBar(innerPadding: PaddingValues, navController: NavController) {
    val myIcon = painterResource(id = R.mipmap.logo)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canPop =
        (backStackEntry?.destination?.route != "downloadedPoetsScreen" && backStackEntry?.destination?.route != "settingsScreen")


    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(innerPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                myIcon,
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(16.dp)) // Add space
            Text(
                text = "جام باده",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            ) // TODO constants may need to be in another file
        }
        if (canPop) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(end = 16.dp)
                    .size(24.dp),
                tint = Color.White

            )
        }
    }
}
