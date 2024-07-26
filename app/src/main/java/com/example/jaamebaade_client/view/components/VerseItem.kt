package com.example.jaamebaade_client.view.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
        ModalBottomSheet(
            onDismissRequest = { showDialog = false },
            sheetState = sheetState,
        ) {
            viewModel.getWordMeaning(
                word = verse.text.substring(startIndex, endIndex),
                successCallBack = {
                    meaningFetchStatus = Status.SUCCESS
                },
                failCallBack = {
                    meaningFetchStatus = Status.FAILED
                })
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                ) {
                    Button(onClick = {
                        highlightCallBack(
                            startIndex,
                            endIndex
                        )
                        showDialog = false

                    }) {
                        Text("هایلایت", style = MaterialTheme.typography.bodySmall)
                    }

                    Button(onClick = {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(
                            "جام سخن",
                            verse.text.substring(startIndex, endIndex)
                        )
                        clipboard.setPrimaryClip(clip)
                        showDialog = false
                    }) {
                        Text("کپی", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = " معنی:"
                    )
                    Text(
                        text = verse.text.substring(startIndex, endIndex),
                        fontWeight = FontWeight.Bold
                    )
                }
                when (meaningFetchStatus) {
                    Status.LOADING, Status.NOT_STARTED -> {
                        Box(modifier = Modifier.height(40.dp)) {
                            LoadingIndicator()
                        }
                    }

                    Status.FAILED -> {
                        Text("خطا در دریافت معنی")
                    }

                    Status.SUCCESS -> {
                        Text(
                            text = meaning,
                            modifier = Modifier.padding(
                                bottom = 10.dp
                            )
                        )
                    }

                    else -> {

                    }
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,


        ) {
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
            modifier = Modifier.padding(0.dp),
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