package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.view.components.FontFamilyMenu
import ir.jaamebaade.jaamebaade_client.view.components.FontSizeTileMenu

@Composable
fun ChangeFontScreen(modifier: Modifier, fontRepository: FontRepository) {

    Column(modifier = modifier.padding(8.dp)) {
        Text(stringResource(R.string.FONT_SIZE_TEXT))
        FontSizeTileMenu(fontRepository)

        Text(stringResource(R.string.FONT_FAMILY_TEXT))
        FontFamilyMenu(fontRepository = fontRepository)

    }
}

