package com.jonandpaul.jonandpaul.ui.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.utils.TextFieldValues

@Composable
fun AuthenticationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit,
    placeholderText: String,
    isObscured: Boolean = false,
    textFieldValues: TextFieldValues,
    modifier: Modifier = Modifier,
    error: String? = null,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    onImeActionClick: (KeyboardActionScope) -> Unit
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
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
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            isError = !error.isNullOrEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = onImeActionClick,
                onDone = onImeActionClick
            )
        )

        error?.let { err ->
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = err,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }
    }
}