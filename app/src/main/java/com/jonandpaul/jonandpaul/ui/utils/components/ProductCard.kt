package com.jonandpaul.jonandpaul.ui.utils.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.theme.Red900
import com.jonandpaul.jonandpaul.ui.utils.twoDecimalsString

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    imageSize: Dp,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .width(imageSize)
                .wrapContentWidth(align = Alignment.End)
                .clip(RoundedCornerShape(10.dp))
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
                modifier = Modifier
                    .size(imageSize)
                    .then(modifier)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 10.dp, top = 10.dp)
                    .then(modifier)
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
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
            Text(text = product.title, modifier = Modifier.then(modifier))

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${product.price.twoDecimalsString()} RON",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.then(modifier)
            )
        }
    }
}