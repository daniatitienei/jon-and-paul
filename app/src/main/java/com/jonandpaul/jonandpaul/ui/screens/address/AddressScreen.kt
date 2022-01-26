package com.jonandpaul.jonandpaul.ui.screens.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {

    var searchBar by remember {
        mutableStateOf("")
    }

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
                    .padding(horizontal = 15.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
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
        LazyColumn(
            contentPadding = PaddingValues(
                start = 15.dp,
                end = 15.dp,
                top = 20.dp,
                bottom = innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                CurrentAddress(address = "Aleea Constructorilor, 5")
            }

            item {
                TextField(
                    value = searchBar,
                    onValueChange = { searchBar = it },
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
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { /*TODO*/ }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.change_address),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(5.dp))

                repeat(20) {
                    Row(
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Aleea Constructorilor, 5", modifier = Modifier.weight(9f))
                        Icon(
                            Icons.Outlined.Place,
                            contentDescription = null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
            onNavigate = {},
            onPopBackStack = {}
        )
    }
}