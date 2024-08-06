package com.example.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.Status
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.utility.toPersianNumber
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel


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
            enter = slideInHorizontally(
                initialOffsetX = { it / 2 }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it / 2 }),
        ) {
            Checkbox(checked = isSelected, onCheckedChange = { onClick() })
        }
        if (showVerseNumber && index % 2 == 0) {
            Text(
                text = (index / 2 + 1).toPersianNumber(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 2.dp)
            )
        }
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                if (it.selection.start != it.selection.end) {
                    startIndex = it.selection.min
                    endIndex = it.selection.max
                    showDialog = true
                }
            },
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
        )
    }
    if (verse.position % 2 == 1)
        Spacer(modifier = Modifier.height(20.dp))

}