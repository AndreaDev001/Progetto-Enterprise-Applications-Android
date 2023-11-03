package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.viewmodel.create.CreateBanViewModel
import com.enterpriseapplications.viewmodel.create.CreateReportViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.util.UUID

@Composable
fun CreateBan(userID: UUID,update: Boolean,confirmCallback: (createdBan: Ban) -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {

    val viewModel: CreateBanViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create a Ban" else "Update Ban";
    val reasons: State<List<String>> = viewModel.reasons.collectAsState()
    val createdBan: State<Ban?> = viewModel.createdBan.collectAsState()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()

    if(createdBan.value != null)
        confirmCallback(createdBan.value!!)

    viewModel.reset()
    viewModel.userID = userID
    viewModel.update = update

    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Warning, contentDescription = null,modifier = Modifier.size(50.dp))
    }, text = {
        Column(modifier = Modifier.padding(5.dp).verticalScroll(ScrollState(0))) {
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the ban", placeHolder = "Write a description...", label = "Description")
            FormDropdown(label = "Reason", supportingText = "Please choose one of the available options", modifier = Modifier.padding(5.dp),formControl = viewModel.reasonControl, items = reasons.value)
            Spacer(modifier = Modifier.height(2.dp))
            CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {viewModel.createBan()})
            CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
        }
    }, confirmButton = {}, dismissButton = {})
}