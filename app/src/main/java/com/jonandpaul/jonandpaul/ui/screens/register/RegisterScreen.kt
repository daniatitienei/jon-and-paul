package com.jonandpaul.jonandpaul.ui.screens.register

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
import androidx.compose.ui.platform.LocalContext
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
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                else -> Unit
            }
        }
    }

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

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

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var isConfirmPasswordObscured by remember {
        mutableStateOf(true)
    }

    var confirmPasswordError by remember {
        mutableStateOf<String?>(null)
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
                viewModel.onEvent(RegisterEvents.OnGoogleClick(idToken = account.idToken!!))
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
                            viewModel.onEvent(RegisterEvents.OnPopBackStack)
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
                trailingIcon = {
                    Icon(Icons.Rounded.MailOutline, contentDescription = null)
                },
                placeholderText = "Email",
                textFieldValues = TextFieldValues.EMAIL,
                error = viewModel.emailError.value,
                imeAction = ImeAction.Next,
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(10.dp))

            AuthenticationTextField(
                value = password,
                onValueChange = {
                    confirmPasswordError = if (confirmPassword != it)
                        context.getString(R.string.confirm_password_error)
                    else null
                    password = it
                },
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
                error = viewModel.passwordError.value,
                imeAction = ImeAction.Next,
                onImeActionClick = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(10.dp))

            AuthenticationTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPasswordError = if (it != password)
                        context.getString(R.string.confirm_password_error)
                    else null
                    confirmPassword = it
                },
                placeholderText = "Confirmare parola",
                textFieldValues = TextFieldValues.PASSWORD,
                isObscured = isConfirmPasswordObscured,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isConfirmPasswordObscured = !isConfirmPasswordObscured
                        }
                    ) {
                        Icon(
                            if (!isConfirmPasswordObscured) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                        )
                    }
                },
                error = confirmPasswordError,
                modifier = Modifier.focusRequester(focusRequester = focusRequester),
                imeAction = ImeAction.Done,
                onImeActionClick = {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    if (confirmPasswordError.isNullOrEmpty())
                        viewModel.onEvent(RegisterEvents.OnRegisterClick(email, password))
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.register),
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
                    viewModel.onEvent(RegisterEvents.OnLoginHereClick)
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.already_have_an_account),
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = stringResource(id = R.string.you_can_login_here),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    JonAndPaulTheme {
        RegisterScreen(onNavigate = {}, onPopBackStack = {})
    }
}