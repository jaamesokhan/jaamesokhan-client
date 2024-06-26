package com.example.jaamebaade_client.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.viewmodel.AccountViewModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val apiResult by viewModel.apiResult
    var showDialog by remember { mutableStateOf(true) } // Add this line

    val context = LocalContext.current // Add this line

    LaunchedEffect(apiResult) { // Add this block
        if (apiResult == true) {
            showDialog = false
            navController.navigate("settingsScreen") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            if (apiResult == false) {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    if (showDialog) {

        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = modifier.padding(16.dp),
                shape = RoundedCornerShape(16.dp)

            ) {
                Column {
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text("نام کاربری") },
                        modifier = Modifier.padding(16.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("رمز عبور") },
                        modifier = Modifier.padding(16.dp),
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            }) {
                                Icon(
                                    imageVector = if (passwordVisibility.value) Icons.Default.Close else Icons.Default.Add,
                                    contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                                )
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                viewModel.handleLogin(
                                    username.value,
                                    password.value.text
                                )
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("ورود")
                        }

                        Button(
                            onClick = {
                                viewModel.handleSignup(
                                    username.value,
                                    password.value.text
                                )
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("ثبت نام")
                        }
                    }
                }
            }
        }
    }
}

