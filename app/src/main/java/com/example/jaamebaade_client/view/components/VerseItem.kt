package com.example.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.utility.toPersianNumber
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel


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

    var annotatedString by remember { mutableStateOf<AnnotatedString?>(null) }
    val highlightColor = MaterialTheme.colorScheme.tertiary

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
        Dialog(onDismissRequest = {
            showDialog = false
        }) {
            try {
                viewModel.getWordMeaning(verse.text.substring(startIndex, endIndex))
            } catch (e: Exception) {
                Log.e("VerseItem", "Error in getting word meaning", e)
            }
            Surface(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.defaultMinSize(
                    minWidth = 300.dp,
                    minHeight = 200.dp
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
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
                    }
                    Row {
                        Text(
                            text = " معنی:"
                        )
                        Text(
                            text = verse.text.substring(startIndex, endIndex),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = meaning
                    )
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
        SelectionContainer(modifier = Modifier.padding(0.dp)) {
            CompositionLocalProvider(
                LocalTextInputService provides null
            ) {

                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        if (it.selection.start != it.selection.end) {
                            startIndex = minOf(it.selection.start, it.selection.end)
                            endIndex = maxOf(it.selection.start, it.selection.end)
                            showDialog = true
                        }
                    },
                    modifier = Modifier.padding(0.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center // Center the text within the TextField
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
        }
    }
    if (verse.position % 2 == 1)
        Spacer(modifier = Modifier.height(20.dp))

}