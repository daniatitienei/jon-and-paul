package com.jonandpaul.jonandpaul.ui.screens.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
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
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(AccountEvents.OnPopBackStack)
                        }
                    ) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.my_account))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
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
                    Text(
                        text = stringResource(id = R.string.my_orders),
                    )
                },
                trailing = {
                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.clickable {
                    viewModel.onEvent(AccountEvents.OnOrdersClick)
                }
            )
        }
    }
}