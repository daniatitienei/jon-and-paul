package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.theme.Red900

@Composable
fun InspectProductScreen() {
    val columnScroll = rememberScrollState()
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(
                    horizontal = 15.dp
                )
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(0.9f)
                ) {
                    Text(text = "Marime: universala")
                }

                Spacer(modifier = Modifier.weight(0.1f))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Icon(
                        Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = "Adauga in cos", color = MaterialTheme.colors.primary)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(columnScroll)
        ) {
            /* Creates the product image with top app bar nested on it */
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cherry),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = { /*TODO*/ },
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
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White),
                        ) {
                            Icon(
                                Icons.Rounded.Favorite,
                                contentDescription = null,
                                tint = Red900
                            )
                        }
                    },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Soseste \"The Kiss\"", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "20 RON", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(10.dp))

                ProductInfoCard()
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Alți utilizatori au achiziționat și",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 15.dp)
            ) {
                items(5) {
                    ProductCard(
                        product = Product(
                            title = "Sosete \"Cherry\"",
                            price = 20
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product
) {
    Column(
        modifier = Modifier.clickable { /*TODO*/ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.cherry),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(start = 5.dp)
        ) {
            Text(text = product.title)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "${product.price} RON")
        }
    }
}

@Composable
private fun ProductInfoCard() {
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
                        append("Bucăți - ")
                    }
                    append("1")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Mărime - ")
                    }
                    append("universala")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Compoziție - ")
                    }
                    append("poliester, bumbac")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InspectProductPreview() {
    JonAndPaulTheme {
        InspectProductScreen()
    }
}