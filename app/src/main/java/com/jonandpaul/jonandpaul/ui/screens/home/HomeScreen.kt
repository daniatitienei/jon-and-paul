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
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.*
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
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
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
                onSelectSizeClick = { index ->
                    scope.launch {
                        viewModel.onEvent(HomeEvents.ShowModalBottomSheet)
                        selectedCartItemIndex = index
                    }
                },
                onNavigationClick = {
                    scope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        else
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            )
        },
        sheetPeekHeight = 0.dp,
    ) {
        BackdropScaffold(
            scaffoldState = backdropScaffoldState,
            backLayerBackgroundColor = MaterialTheme.colors.background,
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
                    onEvent = viewModel::onEvent,
                    onCartClick = {
                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            else
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
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
            IconButton(onClick = onClear) {
                Icon(
                    Icons.Rounded.Clear,
                    contentDescription = null,
                )
            }
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
    onCartClick: () -> Unit,
    onEvent: (HomeEvents) -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
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
                onClick = onCartClick
            ) {
                Icon(
                    Icons.Outlined.ShoppingBag,
                    contentDescription = "Cosul meu",
                    tint = Black900
                )
            }
            IconButton(
                onClick = {
                    onEvent(HomeEvents.OnFavoritesClick)
                }
            ) {
                Icon(
                    Icons.Rounded.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Black900
                )
            }
            IconButton(
                onClick = {
                    onEvent(HomeEvents.OnAccountClick)
                }
            ) {
                Icon(
                    Icons.Rounded.PersonOutline,
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
    onSelectSizeClick: (Int) -> Unit,
    onNavigationClick: () -> Unit,
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://assets3.lottiefiles.com/packages/lf20_cy82iv.json"),
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            LazyColumn(
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                items(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectSizeClick(index + 1)
                            }
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
                            onClick = onNavigationClick
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

            if (true)
            /* TODO: Cart view model*/
                CartWithItems(onSelectSizeClick)
            else
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
private fun CartWithItems(
    onSelectSizeClick: (Int) -> Unit
) {
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