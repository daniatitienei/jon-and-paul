package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.formatAsPhoneNumber

@Composable
fun InspectOrderScreen(
    viewModel: InspectOrderViewModel = hiltViewModel()
) {
    val order = viewModel.state.value.order

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.order) + " #${order.id}") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Text(text = stringResource(id = R.string.articles), fontWeight = FontWeight.Bold)
            }

            items(2) {
                OrderCard()
            }

            item {
                Text(
                    text = stringResource(id = R.string.payment_method),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Ramburs")
            }

            item {
                Text(
                    text = stringResource(id = R.string.billing_info),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "${order.shippingDetails.lastName} ${order.shippingDetails.firstName}")
                Text(text = "${order.shippingDetails.address}, ${order.shippingDetails.postalCode}, ${order.shippingDetails.city}, ${order.shippingDetails.county}")
                Text(text = "+40 ${order.shippingDetails.phone}")
            }

            item {
                Text(
                    text = stringResource(id = R.string.info_about_order),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.subtotal),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.7f
                        )
                    )

                    Text(
                        text = "20,00 RON",
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(align = Alignment.End),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.7f
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.shipping),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.7f
                        )
                    )

                    Text(
                        text = "15,00 RON",
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(align = Alignment.End),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.7f
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.total),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.7f
                        )
                    )

                    Text(
                        text = "35,00 RON",
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(align = Alignment.End),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderCard() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Row(
        modifier = Modifier.height(screenHeight / 5)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cherry),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(text = "Şosete The Wave")
            Text(
                text = stringResource(id = R.string.size) + ": " + stringResource(id = R.string.un),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
            Text(text = "20,00 RON", fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = R.string.quantity) + ": 2")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderCardPreview() {
    OrderCard()
}

@Preview(showBackground = true)
@Composable
private fun InspectOrderPreview() {
    JonAndPaulTheme {
        InspectOrderScreen()
    }
}