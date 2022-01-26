package com.jonandpaul.jonandpaul.ui.screens.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(AccountEvents.OnPopBackStack)
                        }
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.my_account))
                },
                backgroundColor = MaterialTheme.colors.background
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
                    Text(text = stringResource(id = R.string.my_orders))
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable { }
            )

            ListItem(
                icon = {
                    Icon(Icons.Outlined.CreditCard, contentDescription = null)
                },
                text = {
                    Text(text = stringResource(id = R.string.my_cards))
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable { }
            )

            ListItem(
                icon = {
                    Icon(Icons.Outlined.Person, contentDescription = null)
                },
                text = {
                    Text(text = stringResource(id = R.string.my_info))
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable { }
            )

            ListItem(
                icon = {
                    Icon(Icons.Outlined.Logout, contentDescription = null)
                },
                text = {
                    Text(text = stringResource(id = R.string.logout))
                },
                modifier = Modifier.clickable {
                    viewModel.onEvent(AccountEvents.OnLogoutClick)
                }
            )

        }
    }
}