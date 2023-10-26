package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.viewmodel.create.CreateOfferViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import java.util.UUID

@Composable
fun CreateOffer(productID: UUID,update: Boolean,confirmCallback: () -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateOfferViewModel = viewModel(factory = viewModelFactory)
    val text: String = if(!update) "Create an offer" else "Update offer";
    viewModel.productID = productID
    viewModel.update = update
    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
       Icon(imageVector = Icons.Default.LocalOffer, contentDescription = null,modifier = Modifier.size(50.dp))
    }, title = {
       Text(modifier = Modifier.fillMaxWidth(),text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = {
        Column(modifier = Modifier.padding(2.dp)) {
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the offer", placeHolder = "Write a description...", label = "Description")
            CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.priceControl, supportingText = "Write the price of the offer", placeHolder = "Write a text...", label = "Price", keyboardType = KeyboardType.Number)
        }
    }, confirmButton = {
        Button(onClick = {
            viewModel.createOffer()
            confirmCallback()},shape = RoundedCornerShape(10.dp)) {
            Text(text = "Confirm", fontSize = 15.sp)
        }
    }, dismissButton = {
        Button(onClick = {cancelCallback()}, shape = RoundedCornerShape(10.dp)) {
            Text(text = "Cancel",fontSize = 15.sp)
        }
    })
}