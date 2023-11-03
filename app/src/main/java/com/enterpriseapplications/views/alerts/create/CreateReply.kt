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
import androidx.compose.material.icons.filled.Reply
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
import com.enterpriseapplications.model.Reply
import com.enterpriseapplications.viewmodel.create.CreateReplyViewModel
import com.enterpriseapplications.viewmodel.create.CreateReportViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.util.UUID

@Composable
fun CreateReply(reviewID: UUID,update: Boolean,confirmCallback: (createdReply: Reply) -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateReplyViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create a reply" else "Update reply";
    viewModel.reset()
    viewModel.reviewID = reviewID
    viewModel.update = update
    val newReply: State<Reply?> = viewModel.newReply.collectAsState()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    if(newReply.value != null)
        confirmCallback(newReply.value!!)

    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Reply, contentDescription = null,modifier = Modifier.size(50.dp))
    }, text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
            Text(text = "Please provide the text of the reply",fontSize = 15.sp,fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomTextField(formControl = viewModel.textControl,modifier = Modifier.padding(2.dp),label = "Text", placeHolder = "Reply...", supportingText = "Write a new reply")
            }
            Spacer(modifier = Modifier.height(2.dp))
            CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {viewModel.createReply()})
            CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
        }
    }, confirmButton = {}, dismissButton = {})
}