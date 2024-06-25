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
import com.example.jaamebaade_client.model.Verse

@Composable
fun SearchResultItem(modifier: Modifier, result: Verse, navController: NavController) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { navController.navigate("poem/${result.poemId}") }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // TODO add full path in here
                Text(text = result.poemId.toString(), style = MaterialTheme.typography.labelMedium)
                Text(text = ">", style = MaterialTheme.typography.labelMedium)
            }
            Text(text = result.text, style = MaterialTheme.typography.headlineSmall)
        }
    }
}