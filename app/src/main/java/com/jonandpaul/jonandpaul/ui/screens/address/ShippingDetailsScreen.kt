package com.jonandpaul.jonandpaul.ui.screens.address

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.County
import com.jonandpaul.jonandpaul.domain.model.ShippingDetails
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.Resource
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.text_transformations.visual_transformation.PhoneNumberVisualTransformation
import kotlinx.coroutines.flow.collect

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ShippingDetailsScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: ShippingDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var address by remember {
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
        counties.data?.let { counties: List<County> ->
            AlertDialog(
                onDismissRequest = { isCountiesDialogOpen = false },
                text = {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                    ) {
                        items(counties.size) { index ->
                            ListItem(
                                text = {
                                    Text(text = counties[index].nume)
                                },
                                modifier = Modifier.clickable {
                                    isCountiesDialogOpen = false
                                    county = counties[index].nume
                                }
                            )
                        }
                    }
                },
                confirmButton = {},
                icon = {
                    Icon(Icons.Outlined.Place, contentDescription = null)
                }
            )
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.shipping_address))
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(ShippingDetailsEvents.OnNavigationClick) }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                        viewModel.onEvent(
                            ShippingDetailsEvents.OnSaveClick(
                                shippingDetails = ShippingDetails(
                                    address = address,
                                    city = city,
                                    county = county,
                                    firstName = firstName,
                                    lastName = lastName,
                                    phone = phoneNumber,
                                    postalCode = postalCode
                                )
                            )
                        )
                    },
                    enabled = address.isNotEmpty() && city.isNotEmpty() && county.isNotEmpty() &&
                            firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty() &&
                            postalCode.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        color = MaterialTheme.colorScheme.onPrimary
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
                    onImeActionClick = {
                        focusManager.moveFocus(FocusDirection.Right)
                    },
                    modifier = Modifier.weight(0.9f)
                )

                Spacer(modifier = Modifier.weight(0.1f))

                AddressTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholderText = stringResource(id = R.string.last_name),
                    labelText = stringResource(id = R.string.last_name),
                    onImeActionClick = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    modifier = Modifier.weight(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            AddressTextField(
                value = address,
                onValueChange = { address = it },
                placeholderText = stringResource(
                    id = R.string.shipping_address
                ),
                labelText = stringResource(
                    id = R.string.shipping_address_label
                ),
                onImeActionClick = {
                    isCountiesDialogOpen = true
                },
                singleLine = false,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                /* Opens up an alert dialog with all Romania counties */
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
                    onImeActionClick = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    modifier = Modifier.weight(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            AddressTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                placeholderText = stringResource(id = R.string.postal_code),
                labelText = stringResource(id = R.string.postal_code),
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "+40",
                    modifier = Modifier.padding(
                        bottom = 13.dp
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                AddressTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= 9)
                            phoneNumber = it
                    },
                    placeholderText = stringResource(id = R.string.phone),
                    labelText = stringResource(id = R.string.phone),
                    imeAction = ImeAction.Done,
                    onImeActionClick = {
                        keyboardController?.hide()

                        if (address.isNotEmpty() && city.isNotEmpty() &&
                            county.isNotEmpty() && firstName.isNotEmpty() &&
                            lastName.isNotEmpty() && phoneNumber.isNotEmpty() &&
                            postalCode.isNotEmpty()
                        ) {
                            viewModel.onEvent(
                                ShippingDetailsEvents.OnSaveClick(
                                    shippingDetails = ShippingDetails(
                                        address = address,
                                        city = city,
                                        county = county,
                                        firstName = firstName,
                                        lastName = lastName,
                                        phone = phoneNumber,
                                        postalCode = postalCode
                                    )
                                )
                            )

                            Toast.makeText(
                                context,
                                context.getString(R.string.address_saved),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.all_fields_must_be_completed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    keyboardType = KeyboardType.Phone,
                    visualTransformation = PhoneNumberVisualTransformation(),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
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
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = labelText,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            disabledTextColor = MaterialTheme.colorScheme.primary
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Words,
            keyboardType = keyboardType
        ),
        leadingIcon = leadingIcon,
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
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
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