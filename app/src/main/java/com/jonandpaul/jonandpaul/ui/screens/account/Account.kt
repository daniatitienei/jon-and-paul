package com.jonandpaul.jonandpaul.ui.screens.account

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
) {

    var isFeedbackDialogOpen by remember {
        mutableStateOf(false)
    }

    var feedbackText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.AlertDialog -> {
                    isFeedbackDialogOpen = event.isOpen
                }
                is UiEvent.Toast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }
    }

    if (isFeedbackDialogOpen)
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AccountEvents.OnAlertDialog(isOpen = false)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(AccountEvents.OnSendFeedback(message = feedbackText))
                        feedbackText = ""
                    },
                    enabled = feedbackText.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.send))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(AccountEvents.OnAlertDialog(isOpen = false))
                    }
                ) {
                    Text(text = stringResource(id = R.string.close))
                }
            },
            icon = {
                Icon(Icons.Outlined.Feedback, contentDescription = null)
            },
            title = {
                Text(text = stringResource(id = R.string.give_us_feedback))
            },
            text = {
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    maxLines = 2
                )
            },
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(AccountEvents.OnPopBackStack)
                        }
                    ) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.my_account))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            ListItem(
                icon = {
                    Icon(Icons.Outlined.LocalShipping, contentDescription = null)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.my_orders),
                    )
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable {
                    viewModel.onEvent(AccountEvents.OnOrdersClick)
                }
            )

            ListItem(
                icon = {
                    Icon(Icons.Outlined.Feedback, contentDescription = null)
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.feedback),
                    )
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable {
                    viewModel.onEvent(AccountEvents.OnAlertDialog(isOpen = true))
                }
            )
        }
    }
}