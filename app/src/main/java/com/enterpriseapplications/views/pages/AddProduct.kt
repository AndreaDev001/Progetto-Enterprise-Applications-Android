package com.enterpriseapplications.views.pages

import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.viewmodel.AddProductViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
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
            ImageInformation(viewModel = viewModel)
            GeneralInformation(viewModel = viewModel)
            Spacer(modifier = Modifier.height(2.dp))
            CategoryInformation(viewModel = viewModel)
            Spacer(modifier = Modifier.height(2.dp))
            ButtonSection(secondText = "Cancel", firstCallback = {viewModel.confirm()}, secondCallback = {viewModel.reset()})
        }
    }
}
@Composable
private fun ImageInformation(viewModel: AddProductViewModel) {
    Text(modifier = Modifier.padding(vertical = 5.dp),text = "Image Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Text(modifier = Modifier.padding(vertical = 2.dp),text = "Adding images can help your product to stand out between the others", fontSize = 15.sp, fontWeight = FontWeight.Normal)
    val selectedUris: MutableState<List<Uri?>> = remember { mutableStateOf(emptyList())}
    val photoPickerVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
    MultiPhotoPicker(visible = photoPickerVisible.value, callback = {
        selectedUris.value = it
        photoPickerVisible.value = false
    })
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(2.dp), onClick = {photoPickerVisible.value = true}) {
        Text(text = "Add images", fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(5.dp))
    }
    LazyRow(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
        items(selectedUris.value) {
            Column(modifier = Modifier.padding(horizontal = 2.dp).clip(RoundedCornerShape(60))) {
                AsyncImage(contentScale = ContentScale.Crop,modifier = Modifier.size(80.dp),model = it, contentDescription = null)
            }
        }
    }
}
@Composable
private fun GeneralInformation(viewModel: AddProductViewModel) {
    val conditions: State<List<String>> = viewModel.conditions.collectAsState();
    val visibilities: State<List<String>> = viewModel.visibilities.collectAsState()
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
        .fillMaxWidth(),formControl = viewModel.conditionControl, items = conditions.value)
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.visibilityControl,items = visibilities.value)
}

@Composable
private fun CategoryInformation(viewModel: AddProductViewModel) {
    val primaryCategories: State<List<String>> = viewModel.primaryCategories.collectAsState()
    val secondaryCategories: State<List<String>> = viewModel.secondaryCategories.collectAsState()
    val tertiaryCategories: State<List<String>> = viewModel.tertiaryCategories.collectAsState()
    Text(text = "Category Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.updateSecondaries()}, formControl = viewModel.primaryCategoryControl, items = primaryCategories.value)
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.updateTertiaries()}, formControl = viewModel.secondaryCategoryControl, items = secondaryCategories.value)
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.tertiaryCategoryControl, items =  tertiaryCategories.value)
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