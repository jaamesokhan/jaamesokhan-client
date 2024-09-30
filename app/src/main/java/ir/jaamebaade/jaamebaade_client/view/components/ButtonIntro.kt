package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonIntro(title: String, description: String) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}