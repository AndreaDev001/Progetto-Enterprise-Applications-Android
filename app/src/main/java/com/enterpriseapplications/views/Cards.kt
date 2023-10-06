package com.enterpriseapplications.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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


@Composable
fun GenericCard(title: String,userID: UUID,values: List<String>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        AsyncImage(model = "http://10.0.2.2/api/v1/userImages/$userID", contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(60))
                .size(20.dp))
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        values.forEach {value ->
            Text(text = value, fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
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
    Row(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        for(i in 0..amountOfFullStars ) {
            Icon(modifier = Modifier.padding(5.dp),imageVector = Icons.Default.Star,contentDescription = null)
        }
        if(amountOfHalfStars == 1)
            Icon(modifier = Modifier.padding(5.dp),imageVector = Icons.Default.StarHalf,contentDescription = null)
        for(i in 0..amountOfEmptyStars) {
            Icon(modifier = Modifier.padding(5.dp),imageVector = Icons.Default.StarBorder,contentDescription = null)
        }
    }
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
                    AsyncImage(modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),model = "https://images.chesscomfiles.com/uploads/v1/user/212484587.6a37b66e.30x30o.54811ef28c54@2x.png", contentDescription = null)
                    Text(modifier = Modifier.padding(horizontal = 10.dp),text = product.seller.username,fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp))
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
fun UserCard(user: UserDetails,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                AsyncImage(modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(60)),model = "http://10.0.0.2/api/v1/userImages/" + user.id.toString(), contentDescription = null)
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .weight(1f)) {
                Text(text = user.username, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)) {
                    Text(text = user.name, fontSize = 12.sp)
                    Text(text = user.surname, fontSize = 12.sp)
                }
                RatingComponent(rating = user.rating.toInt())
            }
        }
    }
}
@Composable
fun ReportCard(report: Report,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), onClick = {clickCallback()}) {
        GenericCard(title = "Report", userID = report.reported.id, values = listOf(report.description,report.reason,report.type,report.createdDate.toString()))
    }
}
@Composable
fun BanCard(ban: Ban,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), onClick = {clickCallback()}) {
        GenericCard(title = "Ban", userID = ban.banned.id, values = listOf(ban.description,ban.reason,ban.createdDate.toString(),ban.expirationDate.toString()))
    }
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