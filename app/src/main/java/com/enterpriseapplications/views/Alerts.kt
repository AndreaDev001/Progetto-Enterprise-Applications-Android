package com.enterpriseapplications.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.reports.Report


@Composable
fun DescriptionItem(name: String,value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp), horizontalArrangement = Arrangement.Start) {
        Row(horizontalArrangement = Arrangement.Start,modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Text(name,modifier = Modifier.padding(2.dp))
        }
        Row(horizontalArrangement = Arrangement.End,modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Text(value,modifier = Modifier.padding(2.dp))
        }
    }
}
@Composable
fun ReportAlert(report: Report,dismissCallback: () -> Unit = {}) {
    AlertDialog(onDismissRequest = {dismissCallback()}, title = {
        Text(text = "Report")
    }, confirmButton = {}, icon = { Icon(imageVector = Icons.Default.Report, contentDescription = null)}, shape = RoundedCornerShape(10.dp),
    dismissButton = {
        Text(text = "Dismiss", fontSize = 20.sp)
    }, text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalArrangement = Arrangement.Center) {
            DescriptionItem(name = "Reporter", value = report.reporter.username)
            DescriptionItem(name = "Reported", value = report.reported.username)
            DescriptionItem(name = "Type", value = report.type)
            DescriptionItem(name = "Reason", value = report.reason)
            DescriptionItem(name = "Created Date", value = report.createdDate.toString())
        }
    })
}
@Composable
fun BanAlert(ban: Ban,dismissCallback: () -> Unit = {}) {
    AlertDialog(onDismissRequest = {dismissCallback()}, title = {
        Text(text = "Ban")
    }, confirmButton = {}, icon = {Icon(imageVector = Icons.Default.Warning,contentDescription = null)}, shape = RoundedCornerShape(10.dp), dismissButton = {
        Text(text = "Dismiss",fontSize=20.sp)
    }, text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalArrangement = Arrangement.Center) {
            DescriptionItem(name = "Banner", value = ban.banner.username)
            DescriptionItem(name = "Banned", value = ban.banned.username)
            DescriptionItem(name = "Type", value = ban.type)
            DescriptionItem(name = "Reason", value = ban.reason)
            DescriptionItem(name = "Created Date", value = ban.createdDate.toString())
        }
    })
}
@Composable
fun ReviewAlert(review: Review,dismissCallback: () -> Unit) {
    AlertDialog(onDismissRequest = {dismissCallback()}, title = {
        Text(text = "Review")
    }, confirmButton = {}, icon = {Icon(imageVector = Icons.Default.Reviews,contentDescription = null)}, shape = RoundedCornerShape(10.dp), dismissButton = {
        Text(text = "Dismiss",fontSize=20.sp)
    },text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalArrangement = Arrangement.Center) {
            DescriptionItem(name = "Writer", value = review.writer.username)
            DescriptionItem(name = "Receiver", value = review.receiver.username)
            DescriptionItem(name = "Text", value =  review.text)
            DescriptionItem(name = "Created Date", value = review.createdDate.toString())
        }
    })
}
@Composable
fun OfferAlert(offer: Offer,dismissCallback: () -> Unit = {}) {
    AlertDialog(onDismissRequest = {dismissCallback()}, title = {
        Text(text = "Offer")
    }, confirmButton = {}, icon = {Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null)},shape = RoundedCornerShape(10.dp), dismissButton = {
        Text(text = "Dismiss", fontSize = 20.sp)
    },text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalArrangement = Arrangement.Center) {
            DescriptionItem(name = "Buyer", value = offer.buyer.username)
            DescriptionItem(name = "Product", value = offer.product.name)
            DescriptionItem(name = "Description", value = offer.description)
            DescriptionItem(name = "Price", value = offer.price.toString())
            DescriptionItem(name = "Status", value = offer.status)
            DescriptionItem(name = "Expired",value = offer.expired.toString())
            DescriptionItem(name = "Created Date",value = offer.createdDate.toString())
            DescriptionItem(name = "Expiration Date", value = offer.expirationDate.toString())
        }
    })
}