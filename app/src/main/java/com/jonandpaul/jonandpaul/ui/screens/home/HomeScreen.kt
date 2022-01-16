package com.jonandpaul.jonandpaul.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.theme.Red900
import kotlinx.coroutines.awaitCancellation

@ExperimentalFoundationApi
@Composable
fun HomeScreen() {
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
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            items(5) {
                ProductCard()
            }
        }
    }
}

@Composable
private fun ProductCard() {
    Column(
        modifier = Modifier.clickable { /*TODO*/ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Image(painter = painterResource(id = R.drawable.cherry), contentDescription = null)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 10.dp, top = 10.dp)
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Rounded.Favorite, contentDescription = null, tint = Red900)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(start = 5.dp)
        ) {

            Text(text = "Sosete \"Cherry\"")

            Text(text = "20.00 RON")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    ProductCard()
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun HomePreview() {
    JonAndPaulTheme {
        HomeScreen()
    }
}