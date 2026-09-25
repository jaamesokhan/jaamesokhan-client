package ir.jaamebaade.jaamebaade_client.view.components.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.PoemFontSize
import ir.jaamebaade.jaamebaade_client.utility.toPersianNumber
import kotlin.math.roundToInt

@Composable
fun FontSizeSlider(
    percent: Int,
    onPercentChange: (Int) -> Unit,
    onPercentChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val percentText = "${percent.toPersianNumber()}٪"
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space16, vertical = Dimens.space8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = percentText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
        ) {
            Text(
                text = stringResource(R.string.FONT_SIZE_SAMPLE_LETTER),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Slider(
                value = percent.toFloat(),
                onValueChange = { onPercentChange(PoemFontSize.coerce(it.roundToInt())) },
                onValueChangeFinished = onPercentChangeFinished,
                valueRange = PoemFontSize.MIN_PERCENT.toFloat()..PoemFontSize.MAX_PERCENT.toFloat(),
                steps = PoemFontSize.sliderSteps,
                modifier = Modifier
                    .weight(1f)
                    .semantics { stateDescription = percentText },
            )
            Text(
                text = stringResource(R.string.FONT_SIZE_SAMPLE_LETTER),
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        TextButton(
            enabled = percent != PoemFontSize.DEFAULT_PERCENT,
            onClick = {
                onPercentChange(PoemFontSize.DEFAULT_PERCENT)
                onPercentChangeFinished()
            },
        ) {
            Text(text = stringResource(R.string.RESET_TO_DEFAULT))
        }
    }
}
