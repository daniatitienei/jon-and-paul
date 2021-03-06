package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.components.ProductCard
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
    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.init()
    }

    val cartItems = viewModel.state.value.cartItems

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.BackdropScaffold -> {
                    if (backdropScaffoldState.isConcealed)
                        backdropScaffoldState.reveal()
                    else {
                        backdropScaffoldState.conceal()
                    }
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

    val products = viewModel.state.value.products

    val filteredProducts =
        products.filter { product ->
            product.title.lowercase()
                .contains(searchValue.lowercase())
        }

    val state = viewModel.state.value

    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        backLayerBackgroundColor = MaterialTheme.colorScheme.background,
        appBar = {
            HomeTopBar(
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
                cartSize = cartItems.size
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
<<<<<<< HEAD
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.state.value.isLoading) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                    ) {
                        items(10) {
                            ProductCard(
                                product = Product(),
                                onClick = { /*TODO*/ },
                                imageSize = 240.dp,
                                isFavorite = false,
                                onFavoriteClick = { /*TODO*/ },
                                modifier = Modifier.placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer()
                                )
                            )
                        }
                    }
                } else {
                    val items: List<Product> =
                        filteredProducts.ifEmpty { products }

                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                    ) {
                        items(items) { product ->

                            var isFavorite by remember {
                                mutableStateOf(product.isFavorite)
                            }

                            ProductCard(
                                product = product,
                                onClick = {
                                    viewModel.onEvent(HomeEvents.OnProductClick(product = product))
                                },
                                imageSize = 240.dp,
                                isFavorite = isFavorite,
                                onFavoriteClick = {
                                    viewModel.onEvent(
                                        HomeEvents.OnFavoriteClick(
                                            product = product,
                                            isFavorite = isFavorite
                                        )
=======
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
            ) {
                if (!state.isLoading) {
                    val items: List<Product> =
                        filteredProducts.ifEmpty { products }

                    items(items) { product ->
                        var isFavorite by remember {
                            mutableStateOf(product.isFavorite)
                        }

                        ProductCard(
                            product = product,
                            onClick = {
                                viewModel.onEvent(HomeEvents.OnProductClick(product = product))
                            },
                            imageSize = 240.dp,
                            isFavorite = isFavorite,
                            onFavoriteClick = {
                                viewModel.onEvent(
                                    HomeEvents.OnFavoriteClick(
                                        product = product,
                                        isFavorite = isFavorite
>>>>>>> master
                                    )
                                )
                                isFavorite = !isFavorite
                            }
                        )
                    }
                } else {
                    items(10) {
                        ProductCard(
                            product = Product(),
                            onClick = {},
                            imageSize = 240.dp,
                            isFavorite = false,
                            onFavoriteClick = {},
                            modifier = Modifier
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer()
                                )
                        )
                    }
                }
            }
        }
    )

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
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
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
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Rounded.Clear,
                        contentDescription = null,
                    )
                }
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
private fun HomeTopBar(
    backdropScaffoldState: BackdropScaffoldState,
    onSearchClick: () -> Unit,
    onEvent: (HomeEvents) -> Unit,
    cartSize: Int
) {
    SmallTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    if (backdropScaffoldState.isConcealed)
                        Icons.Rounded.Search
                    else
                        Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Black900
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(
                onClick = {
                    onEvent(HomeEvents.OnCartClick)
                }
            ) {
                if (cartSize > 0)
                    BadgedBox(badge = {
                        Badge {
                            Text(
                                text = cartSize.toString(),
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }) {
                        Icon(
                            Icons.Outlined.ShoppingBag,
                            contentDescription = stringResource(id = R.string.my_cart),
                            tint = Black900
                        )
                    }
                else
                    Icon(
                        Icons.Outlined.ShoppingBag,
                        contentDescription = stringResource(id = R.string.my_cart),
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
                    contentDescription = stringResource(id = R.string.favorites),
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
                    contentDescription = stringResource(id = R.string.my_account),
                    tint = Black900
                )
            }
        },
    )
}
