package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.utility.toPersianNumber
import ir.jaamebaade.jaamebaade_client.viewmodel.SelectionOptionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseItem(
    modifier: Modifier = Modifier,
    viewModel: SelectionOptionViewModel = hiltViewModel(),
    verse: Verse,
    index: Int,
    showVerseNumber: Boolean,
    selectMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    highlights: List<Highlight>,
    highlightCallBack: (startIndex: Int, endIndex: Int) -> Unit
) {
    val paddingFromStart = 14.dp
    var showDialog by remember { mutableStateOf(false) }
    var startIndex by remember { mutableIntStateOf(0) }
    var endIndex by remember { mutableIntStateOf(0) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val meaning by viewModel.apiResult
    var meaningFetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }

    var annotatedString by remember { mutableStateOf<AnnotatedString?>(null) }
    val highlightColor = MaterialTheme.colorScheme.tertiary
    val sheetState = rememberModalBottomSheetState()

    val context = LocalContext.current

    fun changeMeaningFetchStatus(status: Status) {
        meaningFetchStatus = status
    }

    LaunchedEffect(key1 = highlights) {
        annotatedString = buildAnnotatedString {
            append(verse.text)
            highlights.forEach {
                addStyle(
                    style = SpanStyle(
                        background = highlightColor,
                        fontWeight = FontWeight.Bold
                    ),
                    start = it.startIndex,
                    end = it.endIndex,
                )
            }
        }
        textFieldValue = TextFieldValue(annotatedString!!)
    }
    if (showDialog) {
        SelectionBottomSheet(
            sheetState = sheetState,
            viewModel = viewModel,
            verse = verse,
            startIndex = startIndex,
            endIndex = endIndex,
            changeMeaningFetchStatus = ::changeMeaningFetchStatus,
            currentMeaningFetchStatus = meaningFetchStatus,
            highlightCallBack = highlightCallBack,
            context = context,
            meaning = meaning
        ) {
            showDialog = false
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,


        ) {
        AnimatedVisibility(
            visible = selectMode,
        ) {
            Checkbox(checked = isSelected, onCheckedChange = { onClick() })
        }

        AnimatedVisibility(visible = showVerseNumber) {
            if (index % 2 == 0) {
                Text(
                    text = (index / 2 + 1).toPersianNumber(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = paddingFromStart)
                )
            } else {
                Spacer(modifier = Modifier.width(paddingFromStart))
            }
        }
        VerseTextField(
            textFieldValue,
            onValueChange = {
                textFieldValue = it
            }
        ) {
            if (it.selection.start != it.selection.end) {
                startIndex = it.selection.min
                endIndex = it.selection.max
                showDialog = true
                // Clear the selection
                textFieldValue = it.copy(selection = TextRange(0))
            }
        }
    }
    if (verse.position % 2 == 1)
        Spacer(modifier = Modifier.height(20.dp))

}

