package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.Poet

@Composable
fun BookmarkItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    poet: Poet,
    poem: Poem,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { navController.navigate("poem/${poet.id}/${poem.id}") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${poem.title} - ${poet.name}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}
