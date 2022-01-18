package com.jonandpaul.jonandpaul.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.theme.Red900
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
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> Unit
            }
        }
    }

    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    var searchValue by remember {
        mutableStateOf("")
    }

    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (backdropScaffoldState.isConcealed) {
                                backdropScaffoldState.reveal()
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            } else {
                                backdropScaffoldState.conceal()
                                focusRequester.freeFocus()
                                keyboardController?.hide()
                            }
                        }
                    }) {
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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.ShoppingBag, contentDescription = "Cosul meu", tint = Black900)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Favorite, contentDescription = "Favorite", tint = Black900)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Person, contentDescription = "Cont", tint = Black900)
                    }
                },
                elevation = 0.dp,
            )
        },
        backLayerContent = {
            TextField(
                value = searchValue,
                onValueChange = { searchValue = it },
                placeholder = {
                    Text(text = "Cautati un articol")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Black900,
                    cursorColor = Black900,
                    leadingIconColor = Black900,
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = focusRequester)
            )
        },
        frontLayerContent = {
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.products.value.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    ) {
                        CircularProgressIndicator(color = Black900)
                    }
                } else {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                    ) {
                        items(viewModel.products.value.products) { product ->
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

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
private fun HomePreview() {
    JonAndPaulTheme {
        HomeScreen({})
    }
}