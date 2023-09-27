package com.enterpriseapplications.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.User


@Composable
fun RatingComponent(rating: Int) {

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Product,clickCallback: () -> Unit = {}) {
    Card(border = BorderStroke(1.dp,Color.Black), shape = RoundedCornerShape(10.dp),modifier = Modifier.fillMaxWidth(), onClick = {clickCallback()}) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start)
            {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(modifier = Modifier.size(20.dp).clip(CircleShape),model = "https://images.chesscomfiles.com/uploads/v1/user/212484587.6a37b66e.30x30o.54811ef28c54@2x.png", contentDescription = null)
                    Text(modifier = Modifier.padding(horizontal = 10.dp),text = product.seller.username,fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp))
                {
                    AsyncImage(modifier = Modifier.fillMaxWidth(),model = "https://images.chesscomfiles.com/uploads/v1/user/212484587.6a37b66e.30x30o.54811ef28c54@2x.png", contentDescription = null)
                    Text(modifier = Modifier.padding(2.dp),text = product.name, fontSize = 12.sp)
                    Text(modifier = Modifier.padding(2.dp),text = product.description, fontSize = 12.sp)
                    Text(modifier = Modifier.padding(2.dp),text = product.brand, fontSize = 12.sp)
                }
            }
        }
    }
}
@Composable
fun UserCard(user: User) {

}