package ir.jaamebaade.jaamebaade_client.view.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R

@Composable
fun AboutUsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val textStyle = MaterialTheme.typography.bodyLarge
    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = context.getString(R.string.ABOUT_US_BASE), style = textStyle)
        Text(text = context.getString(R.string.ABOUT_US_GITHUB), style = textStyle)
        TextButton(onClick = {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.GITHUB_URL))
            )
            context.startActivity(urlIntent)
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Github")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.github_mark),
                    contentDescription = "GitHub",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = context.getString(R.string.ABOUT_US_SUPPORT), style = textStyle)
        TextButton(onClick = {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.HAMIBASH_URL))
            )
            context.startActivity(urlIntent)
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "لینک حامی‌باش")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Filled.Handshake, contentDescription = "Support")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = context.getString(R.string.ABOUT_US_EMAIL), style = textStyle)
        TextButton(onClick = {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + context.getString(R.string.EMAIL))
            )
            context.startActivity(emailIntent)
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = context.getString(R.string.EMAIL))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
            }
        }
    }
}