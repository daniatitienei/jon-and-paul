package com.jonandpaul.jonandpaul.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
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
                    IconButton(onClick = { /*TODO*/ }) {
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
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            RegisterTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(Icons.Rounded.MailOutline, contentDescription = null)
                },
                placeholderText = "Email",
                textFieldValues = TextFieldValues.EMAIL,
            )

            Spacer(modifier = Modifier.height(10.dp))

            RegisterTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                },
                placeholderText = "Parola",
                textFieldValues = TextFieldValues.PASSWORD,
                isObscured = isPasswordObscured,
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordObscured = !isPasswordObscured }
                    ) {
                        Icon(
                            if (isPasswordObscured) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            RegisterTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
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
                            if (isConfirmPasswordObscured) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
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

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(id = R.string.continue_with_google),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = { /*TODO*/ }) {
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

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String,
    isObscured: Boolean = false,
    textFieldValues: TextFieldValues,
    modifier: Modifier = Modifier.fillMaxWidth(),
    error: String? = null
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            placeholder = {
                Text(
                    text = placeholderText,
                    color = Black900.copy(alpha = 0.7f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Black900
            ),
            singleLine = true,
            visualTransformation = if (textFieldValues == TextFieldValues.PASSWORD && isObscured)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
        )

        error?.let { err ->
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = err, color = MaterialTheme.colors.error)
        }
    }
}

private enum class TextFieldValues {
    EMAIL,
    PASSWORD,
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    JonAndPaulTheme {
        RegisterScreen()
    }
}