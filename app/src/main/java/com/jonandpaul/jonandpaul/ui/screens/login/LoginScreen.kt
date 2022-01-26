package com.jonandpaul.jonandpaul.ui.screens.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.api.ApiException
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.TextFieldValues
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.components.AuthenticationTextField
import com.jonandpaul.jonandpaul.ui.utils.components.GoogleButton
import com.jonandpaul.jonandpaul.ui.utils.google_auth.AuthResultContract
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.Navigate -> {
                    onNavigate(event)
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

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordObscured by remember {
        mutableStateOf(true)
    }

    val signInRequestCode = 1

    val googleResultLauncher = rememberLauncherForActivityResult(
        contract = AuthResultContract(),
    ) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            Log.d("firebaseAuthWithGoogle", account?.idToken.toString())

            if (account != null) {
                Log.d("auth_email", account.email!!)
                viewModel.onEvent(LoginEvents.OnContinueWithGoogle(idToken = account.idToken!!))
            }
        } catch (e: ApiException) {
            Log.w("firebaseAuthWithGoogle", "Google sign in failed.", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h6,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(LoginEvents.OnPopBackStack)
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthenticationTextField(
                value = email,
                onValueChange = { email = it },
                placeholderText = "Email",
                textFieldValues = TextFieldValues.EMAIL,
                trailingIcon = {
                    Icon(Icons.Rounded.MailOutline, contentDescription = null)
                },
                modifier = Modifier.focusRequester(focusRequester = focusRequester),
                imeAction = ImeAction.Next,
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                keyboardType = KeyboardType.Email,
                error = viewModel.emailError.value
            )

            Spacer(modifier = Modifier.height(10.dp))

            AuthenticationTextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = "Parola",
                textFieldValues = TextFieldValues.PASSWORD,
                isObscured = isPasswordObscured,
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordObscured = !isPasswordObscured }
                    ) {
                        Icon(
                            if (!isPasswordObscured) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                        )
                    }
                },
                imeAction = ImeAction.Done,
                onImeActionClick = {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                error = viewModel.passwordError.value
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    viewModel.onEvent(LoginEvents.OnLoginClick(email = email, password = password))
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            GoogleButton(
                onClick = {
                    googleResultLauncher.launch(signInRequestCode)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = {
                    viewModel.onEvent(LoginEvents.OnRegisterHereClick)
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.do_not_have_an_account),
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = stringResource(id = R.string.you_can_register_here),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    JonAndPaulTheme {
        LoginScreen(
            onPopBackStack = {},
            onNavigate = {}
        )
    }
}