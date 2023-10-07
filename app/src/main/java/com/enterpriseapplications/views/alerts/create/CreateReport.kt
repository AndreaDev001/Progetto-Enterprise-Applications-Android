package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.viewmodel.create.CreateReportViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.util.UUID

@Composable
fun CreateReport(userID: UUID? = null,productID: UUID? = null,messageID: UUID?,update: Boolean = false,confirmCallback: () -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateReportViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create" else "Update";
    val reasons: State<List<String>> = viewModel.reasons.collectAsState()

    viewModel.userID = userID
    viewModel.productID = productID
    viewModel.messageID = messageID
    viewModel.update = update

    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Warning, contentDescription = null,modifier = Modifier.size(50.dp))
    }, title = {
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(ScrollState(0))) {
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the report", placeHolder = "Write a description...", label = "Description")
            FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.reasonControl, items = reasons.value)
        }
    }, confirmButton = {
        Button(onClick = {confirmCallback()}) {
            Text(text = "Confirm", fontSize = 15.sp)
        }
    }, dismissButton = {
        Button(onClick = {cancelCallback()}) {
            Text(text = "Cancel",fontSize = 15.sp)
        }
    })
}