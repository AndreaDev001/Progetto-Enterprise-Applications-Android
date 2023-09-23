package com.enterpriseapplications.views.alerts

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enterpriseapplications.viewmodel.CreateOfferViewModel
import com.enterpriseapplications.views.pages.ButtonSection
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown

@Composable
fun CreateOffer(productID: Number,update: Boolean,confirmCallback: () -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateOfferViewModel = CreateOfferViewModel()
    val text: String = if(!update) "Create an Offer" else "Update Offer";
    viewModel.productID = productID
    viewModel.update = update
    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
       Icon(imageVector = Icons.Default.Warning, contentDescription = null,modifier = Modifier.size(50.dp))
    }, title = {
       Text(modifier = Modifier.fillMaxWidth(),text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = {
        CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the description of the offer", placeHolder = "Write a description...", label = "Description")
        CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.priceControl, supportingText = "Write the price of the offer", placeHolder = "Write a text...", label = "Price", keyboardType = KeyboardType.Number)
    }, confirmButton = {
        Button(onClick = {confirmCallback()},shape = RoundedCornerShape(10.dp)) {
            Text(text = "Confirm", fontSize = 15.sp)
        }
    }, dismissButton = {
        Button(onClick = {cancelCallback()}, shape = RoundedCornerShape(10.dp)) {
            Text(text = "Cancel",fontSize = 15.sp)
        }
    })
}