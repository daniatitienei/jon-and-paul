package com.jonandpaul.jonandpaul.ui.screens.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.components.ProductCard
import kotlinx.coroutines.flow.collect

@ExperimentalFoundationApi
@Composable
fun FavoritesScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
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

    val favorites = viewModel.state.value.favorites

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(FavoritesEvents.OnNavigationClick)
                        }
                    ) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.favorites))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colors.background
                ),
            )
        }
    ) {
        if (favorites.isNotEmpty())
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
            ) {
                items(favorites) { product ->
                    ProductCard(
                        product = product,
                        onClick = {
                            viewModel.onEvent(FavoritesEvents.OnProductClick(product = product))
                        },
                        imageSize = 240.dp,
                        isFavorite = true,
                        onFavoriteClick = {
                            viewModel.onEvent(FavoritesEvents.OnFavoriteClick(product = product))
                        }
                    )
                }
            }
        else
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Text(text = stringResource(id = R.string.you_do_not_have_any_favorite_article))
            }
    }
}