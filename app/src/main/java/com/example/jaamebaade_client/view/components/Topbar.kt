package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.R

@Composable
fun TopBar(innerPadding: PaddingValues) {
    val myIcon = painterResource(id = R.mipmap.logo)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(innerPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    )
    {
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
}

@Composable
@Preview(showBackground = true)
fun TopBarPreview() {
    TopBar(PaddingValues(16.dp, 16.dp, 16.dp, 16.dp))
}