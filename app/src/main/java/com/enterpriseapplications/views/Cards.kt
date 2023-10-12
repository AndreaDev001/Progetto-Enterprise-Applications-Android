package com.enterpriseapplications.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.reports.Report
import java.util.UUID


data class DescriptionItem(val description: String,val value: String)
@Composable
fun GenericCard(title: String,clickCallback: () -> Unit = {},userID: String,values: List<DescriptionItem>) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}){
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)) {
                Text(text = title,fontSize = 20.sp, fontWeight = FontWeight.Bold);
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(model = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg", contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(60))
                        .size(80.dp))
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)) {
                values.forEach {value ->
                    Text(text = value.description, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 1.dp))
                    Text(text = value.value, fontSize = 12.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}
@Composable
fun RatingComponent(rating: Int) {
    val fullStars: Number = rating / 2;
    val ratingAsString = fullStars.toString();
    var amountOfFullStars: Int = 0;
    var amountOfHalfStars: Int = 0;
    var amountOfEmptyStars: Int = 0;
    val values: List<String> = ratingAsString.split("\\.")
    if(values.size == 1){
        amountOfFullStars = values[0].toInt();
    }
    else if(values.size == 2) {
        amountOfFullStars = values[0].toInt()
        if(values[1].toInt() >= 5)
            amountOfHalfStars = 1;
    }
    amountOfEmptyStars = 10 - amountOfFullStars - amountOfHalfStars;
    Row(modifier = Modifier.fillMaxWidth()) {
        for(i in 0..amountOfFullStars ) {
            Icon(modifier = Modifier.padding(horizontal = 1.dp),imageVector = Icons.Default.Star,contentDescription = null, tint = Color.Yellow)
        }
        if(amountOfHalfStars == 1)
            Icon(modifier = Modifier.padding(horizontal = 1.dp),imageVector = Icons.Default.StarHalf,contentDescription = null,tint = Color.Yellow)
        for(i in 0..amountOfEmptyStars) {
            Icon(modifier = Modifier.padding(horizontal = 1.dp),imageVector = Icons.Default.StarBorder,contentDescription = null,tint = Color.Yellow)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Product,clickCallback: () -> Unit = {}) {
    Card(shape = RoundedCornerShape(5.dp),modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(model = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg", contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(60))
                            .size(40.dp))
                    Text(text = product.seller.username,modifier = Modifier.padding(horizontal = 5.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null,
                modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = product.price.toString(),modifier = Modifier.fillMaxWidth(), fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
                Text(text = product.name, fontSize = 15.sp, fontWeight = FontWeight.Thin)
                Text(text = product.description, fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
@Composable
fun UserCard(user: UserDetails,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(10.dp),onClick = {clickCallback()})
    {
        Column(modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(model = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg", contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(60))
                        .size(80.dp))
            }
            Text(text = user.username, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)) {
                Text(text = user.name,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                Text(text = user.surname,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
            }
            Column(modifier = Modifier.padding(vertical = 2.dp)) {
                Text(text = user.gender,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                RatingComponent(rating = user.rating.toInt())
            }
        }
    }
}
@Composable
fun ReportCard(report: Report,clickCallback: () -> Unit = {}) {
        val description: DescriptionItem = DescriptionItem("Description",report.description)
        val reason: DescriptionItem = DescriptionItem("Reason",report.reason)
        val type: DescriptionItem = DescriptionItem("type",report.type)
        val createdDate: DescriptionItem = DescriptionItem("Created Date",report.createdDate)
        GenericCard(title = "Report",clickCallback = clickCallback, userID = report.reported.id, values = listOf(description,reason,type,createdDate))
}
@Composable
fun BanCard(ban: Ban,clickCallback: () -> Unit = {}) {
        val description: DescriptionItem = DescriptionItem("Description",ban.description)
        val reason: DescriptionItem = DescriptionItem("Reason",ban.reason)
        val createdDate: DescriptionItem = DescriptionItem("Created date",ban.createdDate)
        val expirationDate: DescriptionItem = DescriptionItem("Expiration date",ban.expirationDate)
        GenericCard(title = "Ban", userID = ban.banned.id, values = listOf(description,reason,createdDate,expirationDate))
}
@Composable
fun ReviewCard(review: Review,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                AsyncImage(modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(60)),model = "http://10.0.0.2/api/v1/userImages/" + review.id.toString(), contentDescription = null)
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .weight(1f))
            {
                RatingComponent(rating = review.rating)
                Text(text = review.text,fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
@Composable
fun OfferCard(offer: Offer,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {

        }
    }
}