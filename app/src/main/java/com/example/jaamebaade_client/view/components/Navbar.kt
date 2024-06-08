package com.example.jaamebaade_client.view.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Navbar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Button(onClick = { /* Handle navigation here */ }) {
            Text(text = "Home")
        }
        Button(onClick = { /* Handle navigation here */ }) {
            Text(text = "Profile")
        }
        Button(onClick = { /* Handle navigation here */ }) {
            Text(text = "Settings")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NavbarPreview() {
    Navbar()
}