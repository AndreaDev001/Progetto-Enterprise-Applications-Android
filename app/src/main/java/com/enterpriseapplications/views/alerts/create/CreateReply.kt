package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.viewmodel.create.CreateReplyViewModel
import com.enterpriseapplications.viewmodel.create.CreateReportViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.util.UUID

@Composable
fun CreateReply(reviewID: UUID,update: Boolean,confirmCallback: () -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateReplyViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create a reply" else "Update reply";

    viewModel.reviewID = reviewID
    viewModel.update = update

    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Reply, contentDescription = null,modifier = Modifier.size(50.dp))
    }, title = {
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(ScrollState(0))) {
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.textControl, supportingText = "Write the text of the reply", placeHolder = "Write the text...", label = "Text")
        }
    }, confirmButton = {
        Button(onClick = {
            viewModel.createReply()
            confirmCallback()
        }) {
            Text(text = "Confirm", fontSize = 15.sp)
        }
    }, dismissButton = {
        Button(onClick = {cancelCallback()}) {
            Text(text = "Cancel",fontSize = 15.sp)
        }
    })
}