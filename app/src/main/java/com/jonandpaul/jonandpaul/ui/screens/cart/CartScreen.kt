package com.jonandpaul.jonandpaul.ui.screens.cart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.CartItemEntity
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.ShippingDetails
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.twoDecimals
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
) {
    var currentCartProductId by remember {
        mutableStateOf<Long?>(null)
    }

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val context = LocalContext.current

    val cartItems = viewModel.cartItems.collectAsState(initial = emptyList()).value
    val currentShippingDetails =
        viewModel.currentShippingDetails.collectAsState(initial = null).value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ModalBottomSheet -> {
                    if (modalBottomSheetState.isVisible)
                        modalBottomSheetState.hide()
                    else modalBottomSheetState.show()
                }
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.Toast -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.item_removed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }
    }


    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            LazyColumn(
                contentPadding = PaddingValues(all = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(20) { quantity ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable {
                                currentCartProductId?.let { id ->
                                    viewModel.onEvent(
                                        CartEvents.OnUpdateQuantity(
                                            id = id,
                                            quantity = (quantity + 1).toLong()
                                        )
                                    )
                                }
                                viewModel.onEvent(CartEvents.HideModalBottomSheet)
                            }
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    ) {
                        Text(text = (quantity + 1).toString())
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    title = {
                        Text("Cosul meu")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CartEvents.OnNavigationClick)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = null,
                                tint = Black900
                            )
                        }
                    },
                    elevation = 0.dp
                )
            }
        ) {
            if (cartItems.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 20.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(id = R.string.articles),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        repeat(cartItems.size) { index ->
                            CartItemCard(
                                item = cartItems[index],
                                showQuantityPicker = {
                                    currentCartProductId = cartItems[index].id
                                    viewModel.onEvent(CartEvents.ShowModalBottomSheet)
                                },
                                onEvent = viewModel::onEvent
                            )

                            if (index != cartItems.size - 1)
                                Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.shipping_address),
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(CartEvents.OnAddressClick)
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(9f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.Place, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                currentShippingDetails?.let {
                                    Text(
                                        text = if (it.address.isNotEmpty())
                                            "${it.address}, ${it.postalCode}"
                                        else stringResource(id = R.string.add_address)
                                    )
                                }
                            }
                            Icon(
                                Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .rotate(180f)
                                    .weight(1f)
                                    .size(16.dp),
                            )
                        }
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.payment_method),
                            fontWeight = FontWeight.Bold
                        )

                        PaymentMethod(
                            onClick = { /*TODO*/ },
                            title = stringResource(id = R.string.cash_on_delivery),
                            icon = Icons.Outlined.Payments,
                            isSelected = true
                        )
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(id = R.string.info_about_order),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.subtotal))
                            Text(
                                text = "${viewModel.subtotal.value.twoDecimals()} RON",
                                color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.shipping))
                            Text(
                                text = "15.00 RON",
                                color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.total))
                            Text(
                                text = "${viewModel.total.value.twoDecimals()} RON",
                                style = MaterialTheme.typography.h6
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))


                        Button(
                            onClick = {
                                viewModel.onEvent(CartEvents.OnOrderClick)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 5.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.buy),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                        }

                    }
                }
            } else
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                ) {
                    Text(text = stringResource(id = R.string.cart_is_empty))
                }
        }
    }

}

@Composable
private fun PaymentMethod(
    onClick: () -> Unit,
    title: String,
    icon: ImageVector,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title)
        }
        Icon(
            if (isSelected) Icons.Rounded.RadioButtonChecked else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = null,
            modifier = Modifier
                .weight(1f),
            tint = Black900
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun CartItemCard(
    item: CartItemEntity,
    showQuantityPicker: () -> Unit,
    onEvent: (CartEvents) -> Unit,
) {
    Row {
        Image(
            painter = rememberImagePainter(
                data = item.imageUrl,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .size(150.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(3f)
                ) {
                    Text(text = item.title)

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${stringResource(id = R.string.size)}: ${item.size}",
                        color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = "${item.price.twoDecimals()} RON", fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = {
                        onEvent(CartEvents.OnDeleteProduct(id = item.id))
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Black900.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable {
                            showQuantityPicker()
                        }
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.quantity.toString())
                    Icon(
                        Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = Black900.copy(alpha = 0.3f),
                        modifier = Modifier.rotate(-90f)
                    )
                }
            }
        }

    }
}