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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.enterpriseapplications.model.Reply
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.refs.UserRef
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.views.alerts.create.CreateOffer
import com.enterpriseapplications.views.alerts.create.CreateReply
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.math.BigDecimal
import java.util.UUID


data class DescriptionItem(val description: String,val value: String)
@Composable
fun GenericCard(title: String,clickCallback: () -> Unit = {},userID: UUID,values: List<DescriptionItem>) {
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
                UserImage(userID = userID,size = 80.dp,contentScale = ContentScale.Crop)
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
        .width(200.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    UserImage(userID = product.seller.id, contentScale = ContentScale.Crop)
                    Text(text = product.seller.username,modifier = Modifier.padding(horizontal = 5.dp), fontSize = 12.sp, fontWeight = FontWeight.Thin)
                }
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .heightIn(100.dp, 250.dp)) {
                ProductImage(productID = product.id, contentScale = ContentScale.Crop)
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
                UserImage(userID = user.id, size = 80.dp,contentScale = ContentScale.Crop)
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
        val reporterUsername: DescriptionItem = DescriptionItem("Reporter",report.reporter.username)
        val reportedUsername: DescriptionItem = DescriptionItem("Reported",report.reported.username);
        val description: DescriptionItem = DescriptionItem("Description",report.description)
        val reason: DescriptionItem = DescriptionItem("Reason",report.reason)
        val type: DescriptionItem = DescriptionItem("type",report.type)
        val createdDate: DescriptionItem = DescriptionItem("Created Date",report.createdDate.toString())
        GenericCard(title = "Report",clickCallback = clickCallback, userID = report.reported.id, values = listOf(reporterUsername,reportedUsername,description,reason,type,createdDate))
}
@Composable
fun BanCard(ban: Ban,clickCallback: () -> Unit = {}) {
        val banner: DescriptionItem = DescriptionItem("Banner",ban.banner.username)
        val banned: DescriptionItem = DescriptionItem("Banned",ban.banned.username)
        val reason: DescriptionItem = DescriptionItem("Reason",ban.reason)
        val createdDate: DescriptionItem = DescriptionItem("Created date",ban.createdDate.toString())
        val expirationDate: DescriptionItem = DescriptionItem("Expiration date",ban.expirationDate.toString())
        GenericCard(title = "Ban", userID = ban.banned.id, values = listOf(banner,banned,reason,createdDate,expirationDate))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCard(review: Review,confirmCallback: () -> Unit = {},clickCallback: () -> Unit = {},receiver: Boolean) {
    Card(modifier = Modifier
        .padding(vertical = 2.dp)
        .fillMaxWidth(), shape = RoundedCornerShape(5.dp), onClick = { clickCallback() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = review.writer.username, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                UserImage(modifier = Modifier.padding(horizontal = 10.dp,vertical = 2.dp),userID = review.writer.id, size = 90.dp, contentScale = ContentScale.Crop)
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val creatingReply = remember { mutableStateOf(false)}
                val callback: () -> Unit = {creatingReply.value = false}
                val successCallback: (item: Reply) -> Unit = {
                    callback()
                    confirmCallback()
                }
                if(creatingReply.value) {
                    CreateReply(review.id,update = false,confirmCallback = successCallback, dismissCallback = callback, cancelCallback = callback)
                }
                RatingComponent(rating = review.rating, iconSize = 25.dp)
                Text(
                    text = review.text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = review.createdDate.toString(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                if(review.reply == null) {
                    if(receiver) {
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp), shape = RoundedCornerShape(5.dp), onClick = { creatingReply.value = true }) {
                            Text(
                                text = "Write a reply",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
                else
                {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
                        UserImage(userID = review.reply.writer.id, contentScale = ContentScale.Crop, size = 40.dp)
                        Text(text = review.reply.text,fontSize = 18.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(2.dp))
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCard(order: Order,clickCallback: () -> Unit = {}) {
    Card(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            Column(modifier = Modifier
                .padding(2.dp)
                .weight(1f)) {
                Column(modifier = Modifier.padding(2.dp)) {
                    ProductImage(modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),productID = order.product.id)
                }
                Text(text = order.product.name, fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                Text(text = order.product.description, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
            }
            Column(modifier = Modifier
                .padding(2.dp)
                .weight(1f)) {
                Text(text = "Seller", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(5.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    UserImage(userID = order.product.seller.id, contentScale = ContentScale.Crop)
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
fun OfferCard(navController: NavHostController,offer: Offer,clickCallback: () -> Unit = {},updateCallback: () -> Unit = {},receiver: Boolean = false) {
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
                val createdDate: DescriptionItem = DescriptionItem("Created Date",offer.createdDate.toString())
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)) {
                    DescriptionItem(descriptionItem = offerStatus, headerFontSize = 13.sp, contentTextSize = 13.sp)
                    DescriptionItem(descriptionItem = price, headerFontSize = 13.sp, contentTextSize = 13.sp)
                    DescriptionItem(descriptionItem = createdDate, headerFontSize = 13.sp, contentTextSize = 13.sp)
                }
                val updating = remember { mutableStateOf(false) }
                val callback: () -> Unit = {updating.value = false}
                val confirmCallback: (item: Offer) -> Unit = {
                    callback()
                    updateCallback()
                }
                if(updating.value)
                    CreateOffer(productID = offer.product.id,offerID = offer.id, confirmCallback = confirmCallback, dismissCallback = callback, cancelCallback = callback, update = true)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .horizontalScroll(ScrollState(0))) {
                    if(receiver) {
                        if(offer.status == "OPEN")
                            CustomButton(text = "Update",clickCallback = {updating.value = true})
                    }
                    else
                    {
                        if(offer.status == "ACCEPTED" && offer.product.status == "AVAILABLE")
                            CustomButton(text = "Pay", clickCallback = {navController.navigate("checkoutPage/${offer.product.id}/${offer.price}")})
                        if(offer.status == "OPEN")
                            CustomButton(text = "Update", clickCallback = {updating.value = true})
                    }
                }
            }
        }
    }
}
@Composable
fun MessageCard(message: Message,received: Boolean = false,options: @Composable () -> Unit = {}) {
    val alignment = if(received) Alignment.Start else Alignment.End;
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 3.dp), horizontalAlignment = alignment) {
        Column(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.LightGray).padding(3.dp))
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = message.text, modifier = Modifier.padding(horizontal = 20.dp), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                if(received) {
                    val expanded = remember {mutableStateOf(false)}
                    IconButton(onClick = {expanded.value = true}) {
                        Icon(imageVector = Icons.Default.Info,contentDescription = null,modifier = Modifier.padding(2.dp))
                    }
                    DropdownMenu(expanded = expanded.value, onDismissRequest = {expanded.value = false}) {
                        DropdownMenuItem(text = {options()}, onClick = {})
                    }
                }
            }
            Text(text = message.createdDate.toString(), fontSize = 10.sp, fontWeight = FontWeight.Thin)
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
                  val userID: UUID = if(receiver) conversation.product.seller.id else conversation.starter.id;
                  UserImage(userID = userID,size = 80.dp, contentScale = ContentScale.Crop)
              }
             Column(modifier = Modifier
                 .weight(0.75f)
                 .padding(horizontal = 2.dp)) {
                 val username: String = if(receiver) conversation.product.seller.username else conversation.starter.username
                 Text(text = username, fontSize = 17.sp, fontWeight = FontWeight.Bold)
             }
         }
     }
}
@Composable
fun AddressCard(address: Address) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(40.dp)
            .padding(2.dp),imageVector = Icons.Filled.LocationOn, contentDescription = null)
        Column(modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.padding(2.dp),text = "${address.countryCode},${address.street},${address.locality},${address.postalCode}")
        }
    }
}
@Composable
fun PaymentMethodCard(paymentMethod: PaymentMethod) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(40.dp)
            .padding(2.dp),imageVector = Icons.Filled.CreditCard, contentDescription = null)
        Column(modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.padding(vertical = 2.dp),text = paymentMethod.number)
            Text(modifier = Modifier.padding(vertical = 2.dp),text = paymentMethod.expirationDate.toString())
        }
    }
}
@Composable
fun UserImage(contentScale: ContentScale = ContentScale.None,modifier: Modifier = Modifier,size: Dp = 40.dp,userID: UUID) {
    val path: String = "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/userImages/public/$userID";
    AsyncImage(contentScale = contentScale,modifier = modifier
        .clip(RoundedCornerShape(60))
        .size(size), model = path, contentDescription = null)
}
@Composable
fun ProductImage(contentScale: ContentScale = ContentScale.None,
                 @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, productID: UUID) {
    val path: String = "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/productImages/public/${productID.toString()}/first";
    BetterAsyncImage(modifier = modifier, url = path, contentScale)
}

@Composable
fun BetterAsyncImage(modifier: Modifier = Modifier,url: String,contentScale: ContentScale = ContentScale.None) {
    val loading = remember { mutableStateOf(true) }
    val failed = remember {mutableStateOf(false)}
    if(loading.value)
        ProgressIndicator()
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        if(failed.value)
            AsyncImage(model = "https://img.freepik.com/premium-vector/default-image-icon-vector-missing-picture-page-website-design-mobile-app-no-photo-available_87543-11093.jpg", contentDescription = null,contentScale = contentScale)
        else
        {
            AsyncImage(modifier = modifier,contentScale = contentScale, onError = {
                loading.value = false
                failed.value = true
            },onLoading = {
                loading.value = true
            }, onSuccess = {
                loading.value = false
            }, model = url, contentDescription = null)
        }
    }
}