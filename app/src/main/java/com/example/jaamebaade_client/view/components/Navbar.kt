package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Navbar(navController: NavController) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(color = MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate("downloadedPoetsScreen") }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.size(36.dp)
            )
        }

        IconButton(onClick = { /*TODO: Handle Search click*/ }) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.size(36.dp)
            )
        }

        IconButton(onClick = { navController.navigate("settingsScreen") }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}