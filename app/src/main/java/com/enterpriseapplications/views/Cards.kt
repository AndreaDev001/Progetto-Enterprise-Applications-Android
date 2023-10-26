package com.enterpriseapplications.views

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.config.RetrofitConfig
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Message
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import com.enterpriseapplications.model.reports.Report
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
                UserImage(userID = userID,size = 80.dp)
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)) {
                values.forEach {value ->
                    Text(text = value.description, fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 1.dp))
                    Text(text = value.value, fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}

@Composable
fun DescriptionItem(modifier: Modifier = Modifier,descriptionItem: DescriptionItem, headerFontSize: TextUnit = 12.sp,contentTextSize: TextUnit = 12.sp) {
    Column(modifier = modifier.padding(2.dp)) {
        Text(text = descriptionItem.description, fontSize = headerFontSize, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 1.dp))
        Text(text = descriptionItem.value, fontSize = contentTextSize, fontWeight = FontWeight.Normal,modifier = Modifier.padding(vertical = 1.dp))
    }
}
@Composable
fun RatingComponent(modifier: Modifier = Modifier,rating: Int,iconSize: Dp = 20.dp) {
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
    amountOfEmptyStars = 5 - amountOfFullStars - amountOfHalfStars;
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        for(i in 0 until amountOfFullStars) {
            Icon(imageVector = Icons.Default.Star,contentDescription = null, tint = Color.Yellow,modifier = Modifier.size(iconSize))
        }
        if(amountOfHalfStars == 1)
            Icon(imageVector = Icons.Default.StarHalf,contentDescription = null,tint = Color.Yellow,modifier = Modifier.size(iconSize))
        for(i in 0 until amountOfEmptyStars) {
            Icon(imageVector = Icons.Default.StarBorder,contentDescription = null,tint = Color.Yellow,modifier = Modifier.size(iconSize))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(navHostController: NavHostController,product: Product ,clickCallback: () -> Unit = {}) {
    Card(onClick = {
      val path: String = "productPage/" + product.id
      navHostController.navigate(path)
      clickCallback()
    }, shape = RoundedCornerShape(5.dp),modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    UserImage(userID = product.seller.id)
                    Text(text = product.seller.username,modifier = Modifier.padding(horizontal = 5.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                ProductImage(productID = product.id, modifier = Modifier.fillMaxWidth())
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
fun UserCard(navHostController: NavHostController,user: UserRef,clickCallback: () -> Unit = {}) {
    val userDetails: UserDetails = UserDetails(id = user.id, gender = user.gender,username = user.username, name = user.name, surname = user.surname, rating = user.rating)
    UserCard(navHostController,userDetails)
}
@Composable
fun ProductCard(navHostController: NavHostController,product: ProductRef,clickCallback: () -> Unit = {}) {
    val productDetails: Product = Product(id = product.id,name = product.name, description = product.description, amountOfLikes = product.likes, price = product.price,seller = product.seller);
    ProductCard(navHostController = navHostController,product = productDetails,clickCallback)
}
@Composable
fun UserCard(navHostController: NavHostController, user: UserDetails, clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(10.dp),onClick = {
            val path: String = "profilePage/" + user.id;
            navHostController.navigate(path)
            clickCallback()
    })
    {
        Column(modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                UserImage(userID = user.id, size = 80.dp)
            }
            Text(text = user.username, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            if(user.name != null && user.surname != null) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)) {
                    Text(text = user.name,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                    Text(text = user.surname,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                }
            }
            if(user.rating != null && user.gender != null) {
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(text = user.gender,modifier = Modifier.padding(horizontal = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                    RatingComponent(rating = user.rating.toInt())
                }
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
        .padding(vertical = 2.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = review.writer.username, fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                UserImage(userID = review.writer.id, size = 80.dp)
            }
            Column(modifier = Modifier
                .padding(10.dp), verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally)
            {
                RatingComponent(rating = review.rating)
                Text(text = review.text,fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(vertical = 2.dp))
                Text(text = review.createdDate, fontWeight = FontWeight.Normal, fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
@Composable
fun OrderCard(order: Order,clickCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            Column(modifier = Modifier
                .padding(2.dp)
                .weight(1f)) {
                Column(modifier = Modifier.padding(2.dp)) {
                    ProductImage(productID = order.product.id,modifier = Modifier.fillMaxWidth())
                }
                Text(text = order.product.name, fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                Text(text = order.product.description, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
            }
            Column(modifier = Modifier
                .padding(2.dp)
                .weight(1f)) {
                Text(text = "Seller", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(5.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    UserImage(userID = order.product.seller.id)
                    Text(text = order.product.seller.username,modifier = Modifier.padding(horizontal = 5.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                }
                val priceItem: DescriptionItem = DescriptionItem("Price",order.price.toString())
                val createdDate: DescriptionItem = DescriptionItem("Created Date",order.createdDate.toString());
                DescriptionItem(descriptionItem = priceItem)
                DescriptionItem(descriptionItem = createdDate)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferCard(navController: NavHostController,offer: Offer,clickCallback: () -> Unit = {},receiver: Boolean = false) {
    Card(shape = RoundedCornerShape(5.dp), modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(), onClick = {clickCallback()}) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                ProductCard(navHostController = navController, product = offer.product,clickCallback)
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 2.dp)) {
                val offerStatus: DescriptionItem = DescriptionItem("Status",offer.status)
                val price: DescriptionItem = DescriptionItem("Price",offer.price.toString())
                val createdDate: DescriptionItem = DescriptionItem("Created Date",offer.createdDate)
                val expirationDate: DescriptionItem = DescriptionItem("Expiration Date",offer.expirationDate)
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)) {
                    DescriptionItem(descriptionItem = offerStatus, headerFontSize = 13.sp, contentTextSize = 13.sp)
                    DescriptionItem(descriptionItem = price, headerFontSize = 13.sp, contentTextSize = 13.sp)
                    DescriptionItem(descriptionItem = createdDate, headerFontSize = 13.sp, contentTextSize = 13.sp)
                    DescriptionItem(descriptionItem = expirationDate, headerFontSize = 13.sp, contentTextSize = 13.sp)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .horizontalScroll(ScrollState(0))) {
                    if(receiver) {
                        Button(modifier = Modifier.padding(horizontal = 2.dp),onClick = {}) {
                            Text(text = "Accept", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                        }
                        Button(modifier = Modifier.padding(horizontal = 2.dp),onClick = {}) {
                            Text(text = "Reject", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                        }
                    }
                    else
                    {
                        if(offer.status == "ACCEPTED") {
                            Button(modifier = Modifier.padding(horizontal = 2.dp),onClick = {}) {
                                Text(text = "Pay", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                            }
                        }
                        Button(modifier = Modifier.padding(horizontal = 2.dp),onClick = {}) {
                            Text(text = "Delete", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MessageCard(message: Message,received: Boolean = false,clickCallback: () -> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if(received) Alignment.Start else Alignment.End) {
            Column(modifier = Modifier.padding(2.dp)) {
                Row(modifier = Modifier
                    .padding(vertical = 2.dp)
                    .background(Color.Green)
                    .clip(RoundedCornerShape(10.dp)), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = message.text,modifier = Modifier.padding(horizontal = 20.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    IconButton(onClick = {clickCallback()}) {
                        Icon(imageVector = Icons.Filled.Send,contentDescription = null,modifier = Modifier.padding(horizontal = 2.dp))
                    }
                }
                Text(text = message.createdDate, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp))
            }
        }
    }
}
@Composable
fun ConversationCard(conversation: Conversation,receiver: Boolean,clickCallback: () -> Unit = {}) {
     Button(contentPadding = PaddingValues(5.dp),modifier = Modifier
         .fillMaxWidth()
         .padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}) {
         Row(modifier = Modifier.fillMaxWidth()) {
              Column(modifier = Modifier.weight(0.25f)) {
                  val userID: String = if(receiver) conversation.second.id else conversation.first.id;
                  UserImage(userID = userID,size = 80.dp)
              }
             Column(modifier = Modifier
                 .weight(0.75f)
                 .padding(horizontal = 2.dp)) {
                 val username: String = if(receiver) conversation.second.username else conversation.first.username
                 Text(text = username, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                 Text(text = "Last message here",fontSize = 15.sp, fontWeight = FontWeight.Normal)
             }
         }
     }
}
@Composable
fun AddressCard(address: Address) {
    Row(modifier = Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.size(40.dp).padding(2.dp),imageVector = Icons.Filled.LocationOn, contentDescription = null)
        Column(modifier = Modifier.padding(2.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.padding(2.dp),text = "${address.code},${address.street},${address.locality},${address.postalCode}")
        }
    }
}
@Composable
fun PaymentMethodCard(paymentMethod: PaymentMethod) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.size(40.dp).padding(2.dp),imageVector = Icons.Filled.CreditCard, contentDescription = null)
        Column(modifier = Modifier.padding(2.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.padding(vertical = 2.dp),text = paymentMethod.number)
            Text(modifier = Modifier.padding(vertical = 2.dp),text = paymentMethod.expirationDate)
        }
    }
}
@Composable
fun UserImage(contentScale: ContentScale = ContentScale.None,modifier: Modifier = Modifier,size: Dp = 40.dp,userID: String) {
    val path: String = "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/userImages/public/$userID";
    AsyncImage(contentScale = contentScale,modifier = modifier
        .clip(RoundedCornerShape(60))
        .size(size), model = path, contentDescription = null)
}
@Composable
fun ProductImage(contentScale: ContentScale = ContentScale.None,
                 @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, productID: String) {
    val path: String = "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/productImages/public/$productID/first";
    AsyncImage(contentScale = contentScale,modifier = modifier,model = path, contentDescription = null)
}