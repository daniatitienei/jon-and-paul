package com.jonandpaul.jonandpaul.ui.screens.latest_orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.twoDecimalsString
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun LatestOrdersScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: LatestOrdersViewModel = hiltViewModel()
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.my_orders)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(LatestOrdersEvents.OnNavigationIconClick)
                        }
                    ) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) {
        if (viewModel.state.value.isLoading)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        else
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(viewModel.state.value.latestOrders) { order ->
                    OrderCard(
                        title = stringResource(id = R.string.order) + " #${order.id}",
                        total = order.total.twoDecimalsString(),
                        orderStatus = order.status,
                        onClick = {
                            viewModel.onEvent(LatestOrdersEvents.OnOrderClick(order = order))
                        }
                    )
                }
            }
    }
}

@ExperimentalMaterialApi
@Composable
private fun OrderCard(
    onClick: () -> Unit,
    title: String,
    total: String,
    orderStatus: String
) {
    Card(
        elevation = 2.dp,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    ) {
        ListItem(
            modifier = Modifier
                .requiredHeight(70.dp)
                .fillMaxWidth(),
            text = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            },
            secondaryText = {
                Text(text = "$total RON", fontWeight = FontWeight.Bold)
            },
            trailing = {
                Text(
                    text = orderStatus,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                )
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun LatestOrdersPreview() {
    JonAndPaulTheme {
        LatestOrdersScreen(
            onNavigate = {},
            onPopBackStack = {}
        )
    }
}