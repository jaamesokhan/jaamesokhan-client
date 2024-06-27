package com.example.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel


@Composable
fun VerseItem(
    modifier: Modifier = Modifier,
    viewModel: SelectionOptionViewModel = hiltViewModel(),
    verse: Verse,
    highlights: List<Highlight>,
    highlightCallBack: (startIndex: Int, endIndex: Int) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var startIndex by remember { mutableIntStateOf(0) }
    var endIndex by remember { mutableIntStateOf(0) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val meaning by viewModel.apiResult

    var annotatedString by remember { mutableStateOf<AnnotatedString?>(null) }

    LaunchedEffect(key1 = highlights) {
        annotatedString = buildAnnotatedString {
            append(verse.text)
            highlights.forEach {
                addStyle(
                    style = SpanStyle(
                        color = Color.Red,
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
            Column(modifier = Modifier.background(color = Color.White)) {
                Row {
                    Button(onClick = {
                        highlightCallBack(
                            startIndex,
                            endIndex
                        )
                        showDialog = false

                    }) {
                        Text("هایلایت")
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


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (verse.position % 2 == 0) Arrangement.Start else Arrangement.End

    ) {
        SelectionContainer {
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
                    textStyle = MaterialTheme.typography.bodyMedium,
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
}