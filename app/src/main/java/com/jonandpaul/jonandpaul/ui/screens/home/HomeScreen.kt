package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.*
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.CartProduct
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
    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
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

    val products = viewModel.state.value.products

    val filteredProducts =
        products.filter { product ->
            product.title.lowercase()
                .contains(searchValue.lowercase())
        }


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
                if (viewModel.state.value.isLoading) {
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
                onClick = {
                    onEvent(HomeEvents.OnCartClick)
                }
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
