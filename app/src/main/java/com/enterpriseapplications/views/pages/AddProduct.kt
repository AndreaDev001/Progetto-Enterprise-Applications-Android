package com.enterpriseapplications.views.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.AddProductViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.math.BigInteger


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(navController: NavHostController)
{
    val viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)
    val createdProduct: State<Product?> = viewModel.createdProduct.collectAsState()
    val homeCallback: () -> Unit = {navController.navigate("home");viewModel.reset()};
    if(createdProduct.value != null)
        AlertDialog(onDismissRequest = {homeCallback()}, confirmButton = {}, dismissButton = {},
        icon = {
            Icon(imageVector = Icons.Default.ShoppingCart,contentDescription = null,modifier = Modifier.size(80.dp))
        },text = {
                Column(modifier = Modifier.padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "Product Created",fontSize = 17.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                    Text(text = "Your product has been created successfully", fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
                    Button(shape = RoundedCornerShape(5.dp),modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(), onClick = {navController.navigate("productPage/${createdProduct.value!!.id}");viewModel.reset()}) {
                        Text(text = "Go to product page",modifier = Modifier.padding(2.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    }
                    Button(shape = RoundedCornerShape(5.dp),modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(), onClick = {homeCallback()}) {
                        Text(text = "Go to home page",modifier = Modifier.padding(2.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    }
                }
            })
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
            ImageInformation()
            GeneralInformation()
            Spacer(modifier = Modifier.height(2.dp))
            CategoryInformation()
            Spacer(modifier = Modifier.height(2.dp))
            ButtonSection(viewModel = viewModel)
        }
    }
}
@Composable
private fun ImageInformation() {
    val viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)
    Text(modifier = Modifier.padding(vertical = 5.dp),text = "Image Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Text(modifier = Modifier.padding(vertical = 2.dp),text = "Adding images can help your product to stand out between the others", fontSize = 15.sp, fontWeight = FontWeight.Normal)
    val selectedUris: State<List<Uri?>> = viewModel.currentSelectedUris.collectAsState()
    val photoPickerVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
    Button(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(2.dp), onClick = {
        photoPickerVisible.value = true
    }) {
        SinglePhotoPicker(visible = photoPickerVisible.value) {
            viewModel.updateCurrentSelectedUris(it!!)
            photoPickerVisible.value = false
        }
        Text(text = "Add images", fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(5.dp))
    }
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)) {
        items(selectedUris.value) {
            Column(modifier = Modifier
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(60))) {
                AsyncImage(contentScale = ContentScale.Crop,modifier = Modifier.size(80.dp),model = it, contentDescription = null)
            }
        }
    }
}
@Composable
private fun GeneralInformation() {
    val viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)
    Text(modifier = Modifier.padding(vertical = 5.dp),text = "General Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Text(modifier = Modifier.padding(2.dp),text = "Please provide the general information of the product, like name, description, brand, condition, visibility, price and minimum price for offers")
    val conditions: State<List<String>> = viewModel.conditions.collectAsState();
    val visibilities: State<List<String>> = viewModel.visibilities.collectAsState()
    val currentPriceControl: State<String?> = viewModel.priceControl.currentValue.collectAsState()
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.nameControl, supportingText = "Write the name of the product", placeHolder = "Write a name...", label = "Product Name")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.descriptionControl, supportingText = "Write the description of the product", placeHolder = "Write a description...", label = "Product Description")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.brandControl, supportingText = "Write the brand of the product", placeHolder = "Write a name...", label = "Product Brand")
    CustomTextField(keyboardType = KeyboardType.Number, modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.priceControl, valueCallback = {
         viewModel.minPriceControl.clearValidators()
         viewModel.minPriceControl.addValidator(Validators.required())
         viewModel.minPriceControl.addValidator(Validators.min(BigInteger.valueOf(0)));
         viewModel.minPriceControl.addValidator(Validators.max(it.toBigInteger()))
         viewModel.isValid()
    }, supportingText = "Write the price of the product", placeHolder = "Write a number...", label = "Product Price")
    CustomTextField(enabled = currentPriceControl.value != null && currentPriceControl.value!!.isNotEmpty(), keyboardType = KeyboardType.Number, modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.minPriceControl, supportingText = "Write the minimum price of the product (offers)", placeHolder = "Write a number...",label = "Product Minimum Price (Offer)")
    FormDropdown(label = "Condition", modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.conditionControl, items = conditions.value, supportingText = "Please choose one of the available conditions")
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()}, label = "Visibility", supportingText = "Please choose one of the available visibilities",formControl = viewModel.visibilityControl,items = visibilities.value)
}

@Composable
private fun CategoryInformation() {
    val viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)
    val primaryCategories: State<List<String>> = viewModel.primaryCategories.collectAsState()
    val secondaryCategories: State<List<String>> = viewModel.secondaryCategories.collectAsState()
    val tertiaryCategories: State<List<String>> = viewModel.tertiaryCategories.collectAsState()
    Text(text = "Category Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    FormDropdown(label = "Primary Category", supportingText = "Please choose one of the available primary categories", modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.updateSecondaries();viewModel.isValid()}, formControl = viewModel.primaryCategoryControl, items = primaryCategories.value)
    FormDropdown(label = "Secondary Category", supportingText = "Please choose one of the available secondary categories", modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.updateTertiaries();viewModel.isValid()}, formControl = viewModel.secondaryCategoryControl, items = secondaryCategories.value)
    FormDropdown(label = "Tertiary Category", supportingText = "Please choose one the available tertiary categories", modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), valueCallback = {viewModel.isValid()},formControl = viewModel.tertiaryCategoryControl, items =  tertiaryCategories.value)
}

@Composable
private fun ButtonSection(viewModel: AddProductViewModel = viewModel(factory = viewModelFactory)) {
    val valid: State<Boolean> = viewModel.isValid.collectAsState()
    Button(enabled = valid.value, modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),onClick = {viewModel.confirm()}, shape = RoundedCornerShape(5.dp)) {
        Text(text = "Confirm", fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(2.dp))
    }
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),onClick = {viewModel.reset()}, shape = RoundedCornerShape(5.dp)) {
        Text(text = "Reset", fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(2.dp))
    }
}