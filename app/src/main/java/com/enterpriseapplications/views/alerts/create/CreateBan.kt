package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.reports.MessageReport
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.viewmodel.create.CreateBanViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.DescriptionItem
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import com.enterpriseapplications.views.pages.search.ProgressIndicator

@Composable
fun CreateBan(report: Report?,update: Boolean, confirmCallback: (createdBan: Ban) -> Unit = {}, cancelCallback: () -> Unit = {}, dismissCallback: () -> Unit = {}) {

    val viewModel: CreateBanViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create a Ban" else "Update Ban";
    val reasons: State<List<String>> = viewModel.reasons.collectAsState()
    val createdBan: State<Ban?> = viewModel.createdBan.collectAsState()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()

    if(createdBan.value != null)
        confirmCallback(createdBan.value!!)

    viewModel.reset()
    viewModel.report = report
    viewModel.update = update
    val currentUserReport: State<Report?> = viewModel.currentReport.collectAsState()
    val currentProductReport: State<ProductReport?> = viewModel.currentProductReport.collectAsState()
    val currentMessageReport: State<MessageReport?> = viewModel.currentMessageReport.collectAsState()
    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()},icon = {
        Icon(imageVector = Icons.Default.Report, contentDescription = null,modifier = Modifier.size(80.dp))
    }, title = {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Please provide a reason and a description",fontSize = 15.sp, fontWeight = FontWeight.Thin)
        }
    },text = {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(ScrollState(0))) {
                Column(modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    if(currentUserReport.value != null)
                        UserReportHolder(userReport = currentUserReport.value)
                    if(currentProductReport.value != null)
                        ProductReportHolder(productReport = currentProductReport.value)
                    if(currentMessageReport.value != null)
                        MessageReportHolder(messageReport = currentMessageReport.value)
                }
            Column(modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the ban", placeHolder = "Write a description...", label = "Description")
                FormDropdown(label = "Reason", supportingText = "Please choose one of the available options", modifier = Modifier.padding(5.dp),formControl = viewModel.reasonControl, items = reasons.value)
                Spacer(modifier = Modifier.height(2.dp))
                CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {viewModel.createBan()})
                CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
            }
        }
    }, confirmButton = {}, dismissButton = {})
}
@Composable
private fun MessageReportHolder(messageReport: MessageReport?) {
    if(messageReport != null) {
        val reporterUsername: DescriptionItem = DescriptionItem("Reporter",messageReport.reporter.username)
        val reportedUsername: DescriptionItem = DescriptionItem("Reported",messageReport.reported.username);
        val messageText: DescriptionItem = DescriptionItem("Message Text",messageReport.messageText)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Message information",fontSize = 18.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
            DescriptionItem(descriptionItem = reporterUsername,modifier = Modifier.padding(2.dp))
            DescriptionItem(descriptionItem = reportedUsername,modifier = Modifier.padding(2.dp))
            DescriptionItem(descriptionItem = messageText,modifier = Modifier.padding(2.dp))
        }
    }
}
@Composable
private fun ProductReportHolder(productReport: ProductReport?) {
    if(productReport != null) {
        val reporterUsername: DescriptionItem = DescriptionItem("Reporter",productReport.reporter.username)
        val reportedUsername: DescriptionItem = DescriptionItem("Reported",productReport.reported.username);
        val productName: DescriptionItem = DescriptionItem("Product Name",productReport.product.name)
        val productDescription: DescriptionItem = DescriptionItem("Product Description",productReport.product.description)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Product Information",fontSize = 18.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
            DescriptionItem(headerFontSize = 15.sp, contentTextSize = 15.sp, descriptionItem = reporterUsername,modifier = Modifier.padding(2.dp))
            DescriptionItem(headerFontSize = 15.sp, contentTextSize = 15.sp, descriptionItem = reportedUsername,modifier = Modifier.padding(2.dp))
            DescriptionItem(headerFontSize = 15.sp, contentTextSize = 15.sp, descriptionItem = productName,modifier = Modifier.padding(2.dp))
            DescriptionItem(headerFontSize = 15.sp, contentTextSize = 15.sp, descriptionItem = productDescription,modifier = Modifier.padding(2.dp))
        }
    }
}
@Composable
private fun UserReportHolder(userReport: Report?) {
    if(userReport != null) {
        val reporterUsername: DescriptionItem = DescriptionItem("Reporter",userReport.reporter.username)
        val reportedUsername: DescriptionItem = DescriptionItem("Reported",userReport.reported.username)
        Column(modifier = Modifier.fillMaxWidth()) {
            DescriptionItem(descriptionItem = reporterUsername,modifier = Modifier.padding(2.dp))
            DescriptionItem(descriptionItem = reportedUsername,modifier = Modifier.padding(2.dp))
        }
    }
}