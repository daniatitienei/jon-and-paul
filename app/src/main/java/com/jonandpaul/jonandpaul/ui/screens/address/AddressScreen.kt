package com.jonandpaul.jonandpaul.ui.screens.address

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun AddressScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var newAddress by remember {
        mutableStateOf("")
    }

    val currentAddress = viewModel.currentAddress.collectAsState(initial = "").value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Text(text = stringResource(id = R.string.shipping_address))
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AddressEvents.OnNavigationClick) }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(horizontal = 15.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(AddressEvents.OnSaveClick(newAddress = newAddress))
                    },
                    enabled = newAddress.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                start = 15.dp,
                end = 15.dp,
                top = 20.dp,
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            TextField(
                value = newAddress,
                onValueChange = { newAddress = it },
                placeholder = {
                    Text(
                        text = stringResource(
                            id = R.string.shipping_address_placeholder
                        ),
                        color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (newAddress.isNotEmpty())
                            viewModel.onEvent(AddressEvents.OnSaveClick(newAddress = newAddress))
                        else
                            Toast.makeText(
                                context,
                                context.getString(R.string.empty_address_error),
                                Toast.LENGTH_LONG
                            ).show()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = currentAddress.isNotEmpty(),
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                CurrentAddress(address = currentAddress)
            }
        }
    }
}

@Composable
fun CurrentAddress(address: String) {
    Column {
        Text(
            text = stringResource(id = R.string.current_address),
            color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = address)
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrentAddressPreview() {
    JonAndPaulTheme {
        CurrentAddress(address = "Aleea Constructorilor, 5")
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun AddressPreview() {
    JonAndPaulTheme {
        AddressScreen(
            onPopBackStack = {}
        )
    }
}