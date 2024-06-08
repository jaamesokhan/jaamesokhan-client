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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.ui.theme.lightBrown

@Composable
fun Navbar() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(color = lightBrown)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO: Handle Home click*/ }) {
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

        IconButton(onClick = { /*TODO: Handle Settings click*/ }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NavbarPreview() {
    Navbar()
}