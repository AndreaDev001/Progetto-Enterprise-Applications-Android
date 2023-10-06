package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.viewmodel.AddProductViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.alerts.CreateReport
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(navController: NavHostController)
{
    val viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp, horizontal = 20.dp)) {
        TopAppBar(title = {
            Text(text = "Add Product", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(5.dp))
            GeneralInformation(viewModel = viewModel)
            Spacer(modifier = Modifier.height(2.dp))
            CategoryInformation(viewModel = viewModel)
            Spacer(modifier = Modifier.height(2.dp))
            ButtonSection(secondText = "Cancel", firstCallback = {viewModel.confirm()}, secondCallback = {viewModel.reset()})
        }
    }
}

@Composable
private fun GeneralInformation(viewModel: AddProductViewModel) {
    Text(text = "General Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.nameControl, supportingText = "Write the name of the product", placeHolder = "Write a name...", label = "Product Name")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.descriptionControl, supportingText = "Write the description of the product", placeHolder = "Write a description...", label = "Product Description")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.brandControl, supportingText = "Write the brand of the product", placeHolder = "Write a name...", label = "Product Brand")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.priceControl, supportingText = "Write the price of the product", placeHolder = "Write a number...", label = "Product Price")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.minPriceControl, supportingText = "Write the minimum price of the product (offers)", placeHolder = "Write a number...",label = "Product Minimum Price (Offer)")
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.conditionControl, items = listOf("A","B","C"))
}

@Composable
private fun CategoryInformation(viewModel: AddProductViewModel) {
    Text(text = "Category Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.primaryCategoryControl, items = listOf("A","B","C"))
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.secondaryCategoryControl, items = listOf("A","B","C"))
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.tertiaryCategoryControl, items =  listOf("A","B","C"))
}

@Composable
fun ButtonSection(firstText: String = "Confirm",secondText: String = "Cancel",firstCallback: () -> Unit = {},secondCallback: () -> Unit = {}) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),onClick = {firstCallback()}, shape = RoundedCornerShape(10.dp)) {
        Text(text = firstText, fontSize = 15.sp)
    }
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),onClick = {secondCallback()}, shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = secondText, fontSize = 15.sp)
    }
}