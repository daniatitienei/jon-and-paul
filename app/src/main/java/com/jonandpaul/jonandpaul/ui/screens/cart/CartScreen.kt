package com.jonandpaul.jonandpaul.ui.screens.cart

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.*
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.CartProduct
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
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
    val cartItems = viewModel.state.value.cartItems

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://assets3.lottiefiles.com/packages/lf20_cy82iv.json"),
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    var currentCartProduct by remember {
        mutableStateOf<CartProduct?>(null)
    }

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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
                items(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable {
                                currentCartProduct?.let { item ->
                                    currentCartProduct = viewModel.updateQuantity(
                                        quantity = index + 1,
                                        cartItem = item
                                    )
                                }
                            }
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    ) {
                        Text(text = (index + 1).toString())
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
                    }
                )
            }
        ) {
            if (cartItems.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            showQuantityPicker = {
                                currentCartProduct = item
                                viewModel.onEvent(CartEvents.ShowModalBottomSheet)
                            },
                            onEvent = viewModel::onEvent
                        )
                    }

                    item {
                        Text(text = "Adresa de livrare", style = MaterialTheme.typography.h6)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(9f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.Place, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Aleea Constructorilor 5, Resita")
                            }
                            Icon(
                                Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(180f)
                                    .weight(1f),
                                tint = Black900
                            )
                        }
                    }

                    item {
                        Text(text = "Metoda de plata", style = MaterialTheme.typography.h6)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier.weight(9f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.CreditCard, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = stringResource(id = R.string.add_a_card))
                            }
                            Icon(
                                Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(180f)
                                    .weight(1f),
                                tint = Black900
                            )
                            /* TODO Select card and cash on delivery */
                        }
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.info_about_order),
                            style = MaterialTheme.typography.h6
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(id = R.string.subtotal))
                            Text(
                                text = "40.00 RON",
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
                                text = "65.00 RON",
                                style = MaterialTheme.typography.h6
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))


                        Button(
                            onClick = { /*TODO*/ },
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(composition = composition, progress = progress)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Cosul este gol", style = MaterialTheme.typography.h6)
                }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CartItemCard(
    item: CartProduct,
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
                Column {
                    Text(text = item.title)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Marime: ${item.size}")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "${item.price.twoDecimals()} RON")
                }

                IconButton(
                    onClick = {
                        onEvent(CartEvents.OnDeleteProduct(item = item))
                    }
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