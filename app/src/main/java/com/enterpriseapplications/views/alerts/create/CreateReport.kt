package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.enterpriseapplications.model.reports.MessageReport
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.viewmodel.create.CreateReportViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import com.enterpriseapplications.views.pages.search.SearchingDialog
import java.util.UUID

@Composable
fun CreateReport(userID: UUID? = null, productID: UUID? = null, messageID: UUID? = null, update: Boolean = false, confirmReportCallback: (createdReport: Report?) -> Unit = {},confirmProductReport: (createdReport: ProductReport?) -> Unit = {},confirmMessageReport: (createdReport: MessageReport?) -> Unit = {}, cancelCallback: () -> Unit = {}, dismissCallback: () -> Unit = {}) {
    val viewModel: CreateReportViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create Report" else "Update Report";
    val reasons: State<List<String>> = viewModel.reasons.collectAsState()
    val createdUserReport: State<Report?> = viewModel.createdUserReport.collectAsState()
    val createdProductReport: State<ProductReport?> = viewModel.createdProductReport.collectAsState()
    val createdMessageReport: State<MessageReport?> = viewModel.createdMessageReport.collectAsState()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    val creatingReport: State<Boolean> = viewModel.creatingReport.collectAsState()
    if(creatingReport.value)
        SearchingDialog()

    viewModel.reset()
    viewModel.userID = userID
    viewModel.productID = productID
    viewModel.messageID = messageID
    viewModel.update = update

    if(createdUserReport.value != null)
        confirmReportCallback(createdUserReport.value!!)
    if(createdMessageReport.value != null)
        confirmMessageReport(createdMessageReport.value!!)
    if(createdProductReport.value != null)
        confirmProductReport(createdProductReport.value!!)

    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Warning, contentDescription = null,modifier = Modifier.size(50.dp))
    }, text = {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Please provide a description and a reason",fontSize = 15.sp, fontWeight = FontWeight.Normal)
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the report", placeHolder = "Write a description...", label = "Description")
            FormDropdown(label = "Reason", supportingText = "Please choose one of the available reasons", modifier = Modifier.padding(5.dp),formControl = viewModel.reasonControl, items = reasons.value)
            Spacer(modifier = Modifier.height(2.dp))
            CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {viewModel.createReport()})
            CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
        }
    }, confirmButton = {}, dismissButton = {})
}