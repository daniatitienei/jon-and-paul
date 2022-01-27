package com.jonandpaul.jonandpaul.ui.screens.create_card

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.screens.cart.CartEvents
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.text_transformations.CardNumberVisualTransformation
import com.jonandpaul.jonandpaul.ui.utils.text_transformations.ExpirationDateVisualTransformation
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@Composable
fun CreateCreditCardScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: CreateCreditCardViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.Toast -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.card_added),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Unit
            }
        }
    }

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var owner by remember {
        mutableStateOf("")
    }

    var cardNumber by remember {
        mutableStateOf("")
    }

    var expirationDate by remember {
        mutableStateOf("")
    }

    var cvv by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Text(text = stringResource(id = R.string.add_new_card))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CreateCreditCardEvents.OnNavigationClick)
                        }
                    ) {
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
                        viewModel.onEvent(
                            CreateCreditCardEvents.OnAddCard(
                                owner = owner,
                                number = cardNumber,
                                cvv = cvv,
                                expirationDate = expirationDate
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = (owner.isNotEmpty() && cardNumber.length == 16
                            && expirationDate.length == 4 && cvv.length == 3)
                ) {
                    Text(text = stringResource(id = R.string.save), color = Color.White)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            CreditCardTextField(
                value = owner,
                onValueChange = { owner = it },
                labelText = stringResource(id = R.string.name),
                placeholderText = stringResource(id = R.string.owner_name),
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            CreditCardTextField(
                value = cardNumber,
                onValueChange = {
                    if (it.length < 17)
                        cardNumber = it

                    Log.d(
                        "credit_card",
                        "cardNumber: ${it.length}"
                    )
                },
                labelText = stringResource(id = R.string.card_number),
                placeholderText = "0000 0000 0000 0000",
                trailingIcon = {
                    Icon(Icons.Outlined.CreditCard, contentDescription = null)
                },
                visualTransformation = CardNumberVisualTransformation(),
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CreditCardTextField(
                    value = expirationDate,
                    onValueChange = {
                        if (it.length < 5)
                            expirationDate = it
                        Log.d(
                            "credit_card",
                            "exp: ${it.length}"
                        )
                    },
                    labelText = stringResource(id = R.string.expiration_date),
                    placeholderText = stringResource(id = R.string.month_year),
                    modifier = Modifier.weight(1f),
                    visualTransformation = ExpirationDateVisualTransformation(),
                    onImeActionClick = {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                )

                Spacer(modifier = Modifier.weight(0.1f))

                CreditCardTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length < 4)
                            cvv = it
                    },
                    labelText = stringResource(id = R.string.cvc_cvv),
                    placeholderText = "123",
                    modifier = Modifier.weight(0.9f),
                    imeAction = ImeAction.Done,
                    onImeActionClick = {
                        keyboardController?.hide()
                    }
                )
            }
        }
    }
}

@Composable
private fun CreditCardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    labelText: String,
    placeholderText: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeActionClick: (KeyboardActionScope) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        modifier = modifier,
        label = {
            Text(
                text = labelText,
                color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
            )
        },
        placeholder = {
            Text(
                text = placeholderText,
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            )
        },
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Words
        ),
        keyboardActions = KeyboardActions(
            onNext = onImeActionClick,
            onDone = onImeActionClick
        )
    )
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun CreateCreditCardPreview() {
    JonAndPaulTheme {
        CreateCreditCardScreen(
            onPopBackStack = {}
        )
    }
}