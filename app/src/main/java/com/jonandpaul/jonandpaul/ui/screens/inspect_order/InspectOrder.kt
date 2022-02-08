package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.text_transformations.formatAsPhoneNumber
import com.jonandpaul.jonandpaul.ui.utils.twoDecimalsString
import kotlinx.coroutines.flow.collect

@Composable
fun InspectOrderScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: InspectOrderViewModel = hiltViewModel()
) {
    val order = viewModel.state.value.order

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
                title = { Text(text = stringResource(id = R.string.order) + " #${order.id}") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(InspectOrderEvents.OnNavigationIconClick)
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
                CircularProgressIndicator()
            }
        else
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.articles),
                        fontWeight = FontWeight.Bold
                    )
                }

                items(order.items) {
                    OrderCard(
                        item = it,
                        onClick = {
                            viewModel.onEvent(InspectOrderEvents.OnProductClick(item = it))
                        }
                    )
                }

                item {
                    Text(
                        text = stringResource(id = R.string.payment_method),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = stringResource(id = R.string.cash_on_delivery))
                }

                item {
                    Text(
                        text = stringResource(id = R.string.billing_info),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = "${order.shippingDetails.lastName} ${order.shippingDetails.firstName}")
                    Text(text = "${order.shippingDetails.address}, ${order.shippingDetails.postalCode}, ${order.shippingDetails.city}, ${order.shippingDetails.county}")
                    Text(text = "+40 ${order.shippingDetails.phone.formatAsPhoneNumber()}")

                    Log.d("phone", order.shippingDetails.phone.length.toString())
                }

                item {
                    Text(
                        text = stringResource(id = R.string.info_about_order),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.subtotal),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.7f
                            )
                        )

                        Text(
                            text = "${(order.total - 15).twoDecimalsString()} RON",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(align = Alignment.End),
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.7f
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.shipping),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.7f
                            )
                        )

                        Text(
                            text = "15,00 RON",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(align = Alignment.End),
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.7f
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.total),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.7f
                            )
                        )

                        Text(
                            text = "${order.total.twoDecimalsString()} RON",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(align = Alignment.End),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
    }
}

@Composable
private fun OrderCard(
    item: CartItem,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Row(
        modifier = Modifier
            .height(screenHeight / 5)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberImagePainter(data = item.imageUrl, builder = { crossfade(true) }),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .width(150.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(text = item.title)
            Text(
                text = stringResource(id = R.string.size) + ": " + item.size,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
            Text(text = "${item.price.twoDecimalsString()} RON", fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = R.string.quantity) + ": ${item.quantity}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InspectOrderPreview() {
    JonAndPaulTheme {
        InspectOrderScreen(
            onNavigate = {},
            onPopBackStack = {}
        )
    }
}