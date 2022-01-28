package com.jonandpaul.jonandpaul.ui.screens.address

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.County
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.Resource
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

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    var county by remember {
        mutableStateOf("")
    }

    var city by remember {
        mutableStateOf("")
    }

    var postalCode by remember {
        mutableStateOf("")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var isCountiesDialogOpen by remember {
        mutableStateOf(false)
    }

    val currentAddress = viewModel.currentAddress.collectAsState(initial = "").value
    val counties =
        viewModel.counties.collectAsState(initial = Resource.Success<List<County>>(data = emptyList())).value

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

    if (isCountiesDialogOpen)
        AlertDialog(
            onDismissRequest = { isCountiesDialogOpen = false },
            buttons = {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    items(counties.data!!.size) { index ->
                        ListItem(
                            text = {
                                Text(text = counties.data[index].nume)
                            },
                            modifier = Modifier.clickable {
                                isCountiesDialogOpen = false
                                county = counties.data[index].nume
                            }
                        )
                    }
                }
            }
        )

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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AddressTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholderText = stringResource(id = R.string.first_name),
                    labelText = stringResource(id = R.string.first_name),
                    onImeActionClick = { /*TODO*/ },
                    modifier = Modifier.weight(0.9f)
                )

                Spacer(modifier = Modifier.weight(0.1f))

                AddressTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholderText = stringResource(id = R.string.last_name),
                    labelText = stringResource(id = R.string.last_name),
                    onImeActionClick = { /*TODO*/ },
                    modifier = Modifier.weight(0.9f)
                )
            }

            AddressTextField(
                value = newAddress,
                onValueChange = { newAddress = it },
                placeholderText = stringResource(
                    id = R.string.shipping_address
                ),
                labelText = stringResource(
                    id = R.string.shipping_address_label
                ),
                onImeActionClick = { /*TODO*/ },
                singleLine = false,
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                /* TODO: API request to get all counties */
                AddressTextField(
                    value = county,
                    onValueChange = { county = it },
                    placeholderText = stringResource(id = R.string.county),
                    labelText = stringResource(id = R.string.county),
                    onImeActionClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(0.9f)
                        .clickable {
                            isCountiesDialogOpen = true
                        },
                    enabled = false
                )

                Spacer(modifier = Modifier.weight(0.1f))

                AddressTextField(
                    value = city,
                    onValueChange = { city = it },
                    placeholderText = stringResource(id = R.string.city),
                    labelText = stringResource(id = R.string.city),
                    onImeActionClick = { /*TODO*/ },
                    modifier = Modifier.weight(0.9f)
                )
            }

            AddressTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                placeholderText = stringResource(id = R.string.postal_code),
                labelText = stringResource(id = R.string.postal_code),
                onImeActionClick = { /*TODO*/ },
                keyboardType = KeyboardType.Number
            )


            AddressTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholderText = stringResource(id = R.string.phone),
                labelText = stringResource(id = R.string.phone),
                onImeActionClick = { /*TODO*/ },
                keyboardType = KeyboardType.Phone
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
private fun AddressTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    labelText: String,
    imeAction: ImeAction = ImeAction.Next,
    onImeActionClick: (KeyboardActionScope) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
            )
        },
        label = {
            Text(
                text = labelText,
                color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Words,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = onImeActionClick,
            onNext = onImeActionClick
        ),
        modifier = modifier,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        enabled = enabled,
    )
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