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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.viewmodel.create.CreatePaymentMethodViewModel
import com.enterpriseapplications.viewmodel.profile.PaymentMethodsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown

@Composable
fun CreatePaymentMethod(confirmCallback: (paymentMethod: PaymentMethod) -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreatePaymentMethodViewModel = viewModel(factory = viewModelFactory)
    viewModel.reset()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    val currentBrands: State<List<String>> = viewModel.currentPaymentMethodsBrands.collectAsState()
    val createdPaymentMethod: State<PaymentMethod?> =
        viewModel.createdPaymentMethod.collectAsState()
    if (createdPaymentMethod.value != null)
        confirmCallback(createdPaymentMethod.value!!)
    AlertDialog(
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = { dismissCallback() },
        icon = {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        text = {
            Column(modifier = Modifier.padding(5.dp).verticalScroll(ScrollState(0))
            ) {
                Text(text = "Create Payment Method", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Please specify the owner of the card, the number and the brand",fontSize = 15.sp, fontWeight = FontWeight.Thin)
                CustomTextField(
                    modifier = Modifier.padding(5.dp),
                    formControl = viewModel.holderNameControl,
                    keyboardType = KeyboardType.Text,
                    label = "Holder's name",
                    placeHolder = "Owner's name...",
                    supportingText = "Write the name of the owner of the card"
                )
                CustomTextField(
                    modifier = Modifier.padding(5.dp),
                    formControl = viewModel.numberControl,
                    keyboardType = KeyboardType.Number,
                    label = "Number",
                    placeHolder = "Card's number...",
                    supportingText = "Write the number of the card"
                )
                FormDropdown(
                    label = "Brand",
                    supportingText = "Please choose one of the available options",
                    modifier = Modifier.padding(5.dp),
                    formControl = viewModel.brandControl,
                    items = currentBrands.value
                )
                Spacer(modifier = Modifier.height(2.dp))
                CustomButton(enabled = valid.value, text = "Confirm", clickCallback = { viewModel.createPaymentMethod()})
                CustomButton(text = "Cancel", clickCallback = { cancelCallback() })
            }
        },
        confirmButton = {},
        dismissButton = {})
}