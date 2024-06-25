package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel


@Composable
fun VerseItem(modifier: Modifier = Modifier, verse: Verse, navController: NavController) {
    val viewModel =
        hiltViewModel<SelectionOptionViewModel, SelectionOptionViewModel.SelectionOptionViewModelFactory> { factory ->
            factory.create(verseId = verse.id)
        }

    val verseWithHighlights = viewModel.verse
    var showDialog by remember { mutableStateOf(false) }
    var startIndex by remember { mutableIntStateOf(0) }
    var endIndex by remember { mutableIntStateOf(0) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    var annotatedString by remember { mutableStateOf<AnnotatedString?>(null) }

    LaunchedEffect(key1 = verseWithHighlights) {
        annotatedString = buildAnnotatedString {
            append(verse.text)
            verseWithHighlights?.highlights?.forEach {
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
            Column(modifier = Modifier.background(color = Color.White)) {
                Row {
                    Button(onClick = {
                        viewModel.highlight(
                            verse.id,
                            startIndex,
                            endIndex
                        )
                        showDialog = false
                    }) {
                        Text("هایلایت")
                    }
                }
                Text(
                    text = "معنی: ${
                        verse.text.substring(
                            startIndex,
                            endIndex
                        )
                    }"
                )
                Text(text = "verse id ${verse.id} start index ${startIndex} end index ${endIndex}")
            }
        }
    }

    SelectionContainer {
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                if (it.selection.start != it.selection.end) {
                    startIndex = it.selection.start
                    endIndex = it.selection.end
                    showDialog = true
                }
            },
            modifier = modifier,
            textStyle = MaterialTheme.typography.headlineSmall,
            readOnly = true // make it read-only if you don't want the user to edit the text
        )
    }
}

