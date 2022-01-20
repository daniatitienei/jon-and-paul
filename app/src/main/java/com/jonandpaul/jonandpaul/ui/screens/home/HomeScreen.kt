package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.CardProduct
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.components.ProductCard
import com.jonandpaul.jonandpaul.ui.utils.twoDecimals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
    )

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.ModalBottomSheet -> {
                    if (!modalBottomSheetState.isVisible)
                        modalBottomSheetState.show()
                    else
                        modalBottomSheetState.hide()
                }
                is UiEvent.BottomSheet -> {
                    /* FIXME It doesn't expand */
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    else
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                }
                is UiEvent.BackdropScaffold -> {
                    if (backdropScaffoldState.isConcealed)
                        backdropScaffoldState.reveal()
                    else
                        backdropScaffoldState.conceal()
                }
                else -> Unit
            }
        }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchValue by remember {
        mutableStateOf("")
    }

    val products = viewModel.homeState.value.products

    var selectedCartItemIndex by remember {
        mutableStateOf<Int?>(null)
    }

    val filteredProducts =
        products.filter { product ->
            product.title.lowercase()
                .contains(searchValue.lowercase())
        }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            CartBottomSheet(
                modalBottomSheetState = modalBottomSheetState,
                onEvent = viewModel::onEvent,
                onSelectSizeClick = { index ->
                    scope.launch {
                        viewModel.onEvent(HomeEvents.ShowModalBottomSheet)
                        selectedCartItemIndex = index
                    }
                }
            )
        },
        sheetPeekHeight = 0.dp,
    ) {
        BackdropScaffold(
            scaffoldState = backdropScaffoldState,
            appBar = {
                TopBar(
                    backdropScaffoldState = backdropScaffoldState,
                    onSearchClick = {
                        if (backdropScaffoldState.isConcealed) {
                            viewModel.onEvent(HomeEvents.RevealBackdrop)
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        } else {
                            viewModel.onEvent(HomeEvents.ConcealBackdrop)
                            focusRequester.freeFocus()
                            keyboardController?.hide()
                        }
                    },
                    onEvent = viewModel::onEvent
                )
            },
            backLayerContent = {
                SearchBar(
                    value = searchValue,
                    onValueChange = { searchValue = it },
                    focusRequester = focusRequester,
                    onDone = {
                        scope.launch {
                            viewModel.onEvent(HomeEvents.ConcealBackdrop)
                            keyboardController?.hide()
                        }
                    },
                    onClear = { searchValue = "" }
                )
            },
            frontLayerContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (viewModel.homeState.value.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        ) {
                            CircularProgressIndicator(color = Black900)
                        }
                    } else {
                        val items: List<Product> =
                            if (filteredProducts.isNotEmpty()) filteredProducts else products

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                        ) {
                            items(items) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {
                                        viewModel.onEvent(HomeEvents.OnProductClick(product = product))
                                    },
                                    imageSize = 240.dp
                                )
                            }
                        }
                    }
                }
            }
        )

    }

}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onDone: (KeyboardActionScope) -> Unit,
    placeholder: String = "Cautati un articol",
    focusRequester: FocusRequester,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Black900,
            cursorColor = Black900,
            leadingIconColor = Black900,
            trailingIconColor = Black900
        ),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = null)
        },
        trailingIcon = {
            Icon(
                Icons.Rounded.Clear,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClear() }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester = focusRequester),
        keyboardActions = KeyboardActions(
            onDone = onDone
        )
    )
}

@ExperimentalMaterialApi
@Composable
private fun TopBar(
    backdropScaffoldState: BackdropScaffoldState,
    onSearchClick: () -> Unit,
    onEvent: (HomeEvents) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    if (backdropScaffoldState.isConcealed)
                        Icons.Rounded.Search
                    else
                        Icons.Rounded.Close,
                    contentDescription = "Cautare",
                    tint = Black900
                )
            }
        },
        title = {
            Text(text = "Jon & Paul")
        },
        actions = {
            IconButton(
                onClick = {
                    onEvent(HomeEvents.ExpandBottomSheet)
                }
            ) {
                Icon(
                    Icons.Outlined.ShoppingBag,
                    contentDescription = "Cosul meu",
                    tint = Black900
                )
            }
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Rounded.Favorite,
                    contentDescription = "Favorite",
                    tint = Black900
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = "Cont",
                    tint = Black900
                )
            }
        },
        elevation = 0.dp,
    )
}

@ExperimentalMaterialApi
@Composable
private fun CartBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    onEvent: (HomeEvents) -> Unit,
    onSelectSizeClick: (Int) -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            LazyColumn {
                items(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(HomeEvents.HideModalBottomSheet)
                            }
                            .padding(10.dp)
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
                    title = {
                        Text("Cosul meu")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onEvent(HomeEvents.CollapseBottomSheet)
                            }
                        ) {
                            Icon(
                                Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null,
                                tint = Black900,
                                modifier = Modifier.rotate(-90f)
                            )
                        }
                    }
                )
            }
        ) {
            /* TODO: Cart */
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(1) { index ->
                    CartProductCard(
                        product = CardProduct(
                            title = "Sosete \"Cherry\"",
                            price = 20.00,
                            amount = 1
                        ),
                        onSelectSizeClick = {
                            onSelectSizeClick(index)
                        }
                    )
                }

                item {
                    Text(text = "Adresa de livrare", style = MaterialTheme.typography.h6)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
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
                        Text(text = "Subtotal")
                        Text(
                            text = "40.00 RON",
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Livrare")
                        Text(
                            text = "15.00 RON",
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total")
                        Text(
                            text = "65.00 RON",
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.buy),
                                color = MaterialTheme.colors.onSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CartProductCard(
    product: CardProduct,
    onSelectSizeClick: () -> Unit,
) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.cherry),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
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
                    Text(text = product.title)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Marime: ${product.size}")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "${product.price.twoDecimals()} RON")
                }
                IconButton(onClick = { /*TODO*/ }) {
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
                            onSelectSizeClick()
                        }
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = product.amount.toString())
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

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun CartProductCardPreview() {
    JonAndPaulTheme {
        CartProductCard(
            product = CardProduct(
                title = "Sosete \"The Kiss\"",
                amount = 1,
                price = 20.00,
            ),
            {}
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun CartPreview() {
    JonAndPaulTheme {
        CartBottomSheet(
            modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
            onEvent = {},
            onSelectSizeClick = {}
        )
    }
}