package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.Red900
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.components.ProductCard
import com.jonandpaul.jonandpaul.ui.utils.twoDecimals
import kotlinx.coroutines.flow.collect

@ExperimentalMaterial3Api
@Composable
fun InspectProductScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    product: Product,
    viewModel: InspectProductViewModel = hiltViewModel()
) {
    val columnScroll = rememberScrollState()


    val suggestions = viewModel.state.value.suggestions

    var isFavorite by remember {
        mutableStateOf(
            product.isFavorite
        )
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                is UiEvent.Toast -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.item_added_to_cart),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                onAddToCartClick = {
                    viewModel.onEvent(InspectProductEvents.OnAddToCartClick(product = product))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(columnScroll)
        ) {
            HeaderImageWithTopAppBar(
                product = product,
                onEvent = viewModel::onEvent,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    viewModel.onEvent(
                        InspectProductEvents.OnFavoriteClick(
                            product = product
                        )
                    )
                    isFavorite = !isFavorite
                },
            )

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = product.title,
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "${product.price.twoDecimals()} RON",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProductInfoCard(
                    amount = product.amount,
                    size = product.modelSizeInfo,
                    composition = product.composition
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.other_users_have_bought),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (suggestions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 15.dp)
                ) {
                    items(suggestions.size / 2) { index ->
                        ProductCard(
                            product = suggestions[index],
                            onClick = {
                                viewModel.onEvent(
                                    InspectProductEvents.OnProductClick(product = suggestions[index])
                                )
                            },
                            imageSize = 220.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    onAddToCartClick: () -> Unit,
    onSizeClick: () -> Unit = {}
) {
    BottomAppBar(
        contentPadding = PaddingValues(
            horizontal = 15.dp
        ),
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        OutlinedButton(
            onClick = onSizeClick,
            modifier = Modifier.weight(0.9f)
        ) {
            Text(
                text = stringResource(id = R.string.size) + ": " + stringResource(id = R.string.un),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        Button(
            onClick = onAddToCartClick,
            modifier = Modifier.weight(0.9f),
        ) {
            Icon(
                Icons.Outlined.ShoppingBag,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = stringResource(id = R.string.add_to_cart),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
            )
        }
    }
}

/* Creates the product image with top app bar nested on it */
@Composable
private fun HeaderImageWithTopAppBar(
    product: Product,
    onEvent: (InspectProductEvents) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp / 1.5).dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data = product.imageUrl,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(500.dp)
        )
        SmallTopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onEvent(InspectProductEvents.OnPopBackStack) },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White),
                ) {
                    Icon(
                        Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White),
                ) {
                    Icon(
                        if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = Red900
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
private fun ProductInfoCard(
    amount: Int,
    size: String,
    composition: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${stringResource(id = R.string.pieces)} - ")
                    }
                    append(amount.toString())
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${stringResource(id = R.string.size)} - ")
                    }
                    append(size)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${stringResource(id = R.string.composition)} - ")
                    }
                    append(composition)
                }
            )
        }
    }
}