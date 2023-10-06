package com.enterpriseapplications.views.pages.search

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.search.SearchProductsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.lists.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProducts(navController: NavHostController) {
    val viewModel: SearchProductsViewModel = viewModel(factory = viewModelFactory)
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Search Products", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        ModalNavigationDrawer(gesturesEnabled = true, drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(drawerShape = RectangleShape){
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)) {
                    MenuItem(callback = {scope.launch {drawerState.close()}}, trailingIcon = Icons.Filled.Close, headerText = "Filters" , supportingText = "Use the following filters to find the desired products", leadingIcon = null)
                    Spacer(modifier = Modifier.height(10.dp))
                    FilterOptions(viewModel = viewModel)
                }
            }
        }) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {scope.launch {drawerState.open()}}, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Filters", fontSize = 16.sp)
                }
                ItemsList(products = mutableListOf())
            }
        }
    }
}

@Composable
private fun FilterOptions(viewModel: SearchProductsViewModel)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
        GeneralInformation(viewModel = viewModel)
        Spacer(modifier = Modifier.height(5.dp))
        CategoryInformation(viewModel = viewModel)
    }
}

@Composable
private fun GeneralInformation(viewModel: SearchProductsViewModel) {
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.nameControl, supportingText = "Write the product's name", label = "Product Name", placeHolder = "Write a name...")
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the product's description",label = "Product Description", placeHolder = "Write a description...")
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.brandControl, supportingText = "Write the product's brand",label = "Product Brand", placeHolder = "Write a brand...")
    FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.conditionControl, items = listOf("A","B","C"), label = "Condition")
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.minPriceControl, supportingText = "Write the minimum price of the product", placeHolder = "Write a number...", label = "Product Minimum Price", keyboardType = KeyboardType.Number)
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.maxPriceControl, supportingText = "Write the maximum price of the product", placeHolder = "Write a number...", label = "Product Maximum Price", keyboardType = KeyboardType.Number)
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.minLikesControl, supportingText = "Write the minimum likes of the product", placeHolder = "Write a number...", label = "Product Minimum Likes", keyboardType = KeyboardType.Number)
    CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.maxLikesControl, supportingText = "Write the maximum likes of the product", placeHolder = "Write a number...", label = "Product Maximum Likes", keyboardType = KeyboardType.Number)
}

@Composable
private fun CategoryInformation(viewModel: SearchProductsViewModel) {
    FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.primaryCategoryControl, items = listOf("A","B","C"), label = "Primary Category")
    FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.secondaryCategoryControl, items = listOf("A","B","C"), label = "Secondary Category")
    FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.tertiaryCategoryControl, items = listOf("A","B","C"), label = "Tertiary Category")
}

@Composable
private fun ItemsList(products: MutableList<Product>) {
    Box(modifier = Modifier.padding(5.dp)) {
        val product: Product= Product("test","test","test","test",10,10,10,"test","test","test",
            UserDetails("marchioandrea02@gmail.com","andrea","marchio","andreadev01","MALE"))
        for(i in 0..20){
            products.add(product)
        }
        LazyVerticalGrid(modifier = Modifier.padding(5.dp), columns = GridCells.Fixed(2), verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.SpaceBetween, content = {
            itemsIndexed(items = products) {index, item ->
                Box(modifier = Modifier.padding(5.dp)) {
                    ProductCard(product = item)
                }
            }
        })
    }
}