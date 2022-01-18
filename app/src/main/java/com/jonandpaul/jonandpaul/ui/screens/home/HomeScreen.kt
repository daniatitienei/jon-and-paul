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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.theme.Red900
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.collect

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

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Search, contentDescription = "Cautare")
                    }
                },
                title = {
                    Text(text = "Jon & Paul")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.ShoppingBag, contentDescription = "Cosul meu")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Favorite, contentDescription = "Favorite")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Person, contentDescription = "Cont")
                    }
                },
                elevation = 5.dp
            )
        }
    ) { innerPadding ->
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
                modifier = Modifier.padding(innerPadding)
            ) {
                items(viewModel.products.value.products) { product ->
                    ProductCard(
                        product = product,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onEvent: (HomeEvents) -> Unit
) {
    var isFavorite by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.clickable {
            onEvent(HomeEvents.OnProductClick(product = product))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Image(
                painter = rememberImagePainter(
                    data = product.imageUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.size(240.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 10.dp, top = 10.dp)
            ) {
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        if (!isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = Red900,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(start = 5.dp)
        ) {
            Text(text = product.title)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "${"${product.price}".padEnd(5, '0')} RON")
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun HomePreview() {
    JonAndPaulTheme {
        HomeScreen({})
    }
}