package ir.jaamebaade.jaamebaade_client.view.components.setting


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95

/**
 * This component is used in poet detail screen to show a list of items (category or poem).
 */
@Composable
fun MenuItem(
    title: String,
    showDivider: Boolean,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        onClick = onClick,
        color = MaterialTheme.colorScheme.neutralN95
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(start = 20.dp, end = 0.dp)) {
                leadingIcon()
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 20.dp, start = 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )

                }
                if (showDivider) {
                    HorizontalDivider(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}