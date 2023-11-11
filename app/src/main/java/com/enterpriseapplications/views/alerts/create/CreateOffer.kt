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
import androidx.compose.material.icons.filled.LocalOffer
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.viewmodel.create.CreateOfferViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.enterpriseapplications.views.pages.search.SearchingDialog
import java.util.UUID

@Composable
fun CreateOffer(productID: UUID,offerID: UUID? = null,update: Boolean,confirmCallback: (createdOffer: Offer) -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateOfferViewModel = viewModel(factory = viewModelFactory)
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    val text: String = if(!update) "Create an offer" else "Update offer";
    viewModel.productID = productID
    viewModel.offerID = offerID
    viewModel.update = update
    viewModel.reset()
    val creatingOffer: State<Boolean> = viewModel.creatingOffer.collectAsState()
    if(creatingOffer.value)
       SearchingDialog()
    val newOffer: State<Offer?> = viewModel.newOffer.collectAsState()
    val buyer: State<Boolean> = viewModel.buyer.collectAsState()
    if(newOffer.value != null)
        confirmCallback(newOffer.value!!)
    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
       Icon(imageVector = Icons.Default.LocalOffer, contentDescription = null,modifier = Modifier.size(50.dp))
    },text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = text,
                modifier = Modifier.padding(2.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (buyer.value) {
                Text(
                    text = "Please specify a new description and a price",
                    modifier = Modifier.padding(2.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                CustomTextField(
                    label = "Description",
                    placeHolder = "Write a new description...",
                    supportingText = "Write a new description",
                    formControl = viewModel.descriptionControl,
                    modifier = Modifier.padding(2.dp)
                )
                CustomTextField(
                    keyboardType = KeyboardType.Number,
                    label = "Price",
                    placeHolder = "Write a new price...",
                    supportingText = "Write a new price",
                    formControl = viewModel.priceControl,
                    modifier = Modifier.padding(2.dp)
                )
            } else {
                Text(text = "Please either accept or reject the offer", modifier = Modifier.padding(2.dp), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                FormDropdown(
                    label = "Offer Status",
                    formControl = viewModel.offerStatus,
                    supportingText = "Please choose one of the available options",
                    items = listOf("ACCEPTED", "REJECTED"),
                    modifier = Modifier.padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {
                if(update)
                    viewModel.updateOffer();
                else
                    viewModel.createOffer()
            })
            CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
        }
    }, confirmButton = {}, dismissButton = {})
}