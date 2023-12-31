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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.search.SearchProductsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.lists.MenuItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchProducts(navController: NavHostController) {
    val viewModel: SearchProductsViewModel = viewModel(factory = viewModelFactory)
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp)
    ) {
        TopAppBar(title = {
            Text(text = "Search Products", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        val controller: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current;
        val refreshState: SwipeRefreshState = SwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = refreshState, onRefresh = { viewModel.initialize() }) {
            ModalNavigationDrawer(
                gesturesEnabled = false,
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(drawerShape = RectangleShape) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            MenuItem(
                                callback = {
                                    scope.launch {
                                        controller?.hide()
                                        drawerState.close()
                                    }
                                },
                                trailingIcon = Icons.Filled.Close,
                                headerText = "Filters",
                                supportingText = "Use the following filters to find the desired products",
                                leadingIcon = null
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilterOptions(viewModel = viewModel)
                        }
                    }
                }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { scope.launch { drawerState.open() } }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Filters", fontSize = 16.sp)
                    }
                    Text(modifier = Modifier.padding(horizontal = 5.dp),text = "Use the available filters to find the desired products",fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    ItemsList(navController = navController)
                }
            }
        }
    }
}


@Composable
private fun FilterOptions(viewModel: SearchProductsViewModel)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)
        .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally)
    {
        GeneralInformation()
        Spacer(modifier = Modifier.height(2.dp))
        CategoryInformation(viewModel = viewModel)
    }
}

@Composable
private fun GeneralInformation() {
    val viewModel: SearchProductsViewModel = viewModel(factory = viewModelFactory)
    val conditions: State<List<String>> = viewModel.conditions.collectAsState()
    Column(modifier = Modifier.padding(5.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "General Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "General information about the product, name, description,brand,condition,minimum and maximum price,minimum and maximum likes", fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false);viewModel.nameControl.updateValue(it)},formControl = viewModel.nameControl, supportingText = "Write the product's name", label = "Product Name", placeHolder = "Write a name...")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.descriptionControl, supportingText = "Write the product's description",label = "Product Description", placeHolder = "Write a description...")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false )},formControl = viewModel.brandControl, supportingText = "Write the product's brand",label = "Product Brand", placeHolder = "Write a brand...")
        FormDropdown(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.conditionControl, items = conditions.value, label = "Condition", supportingText = "Please choose one of the available conditions")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.minPriceControl, supportingText = "Write the minimum price of the product", placeHolder = "Write a number...", label = "Product Minimum Price", keyboardType = KeyboardType.Number)
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.maxPriceControl, supportingText = "Write the maximum price of the product", placeHolder = "Write a number...", label = "Product Maximum Price", keyboardType = KeyboardType.Number)
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.minLikesControl, supportingText = "Write the minimum likes of the product", placeHolder = "Write a number...", label = "Product Minimum Likes", keyboardType = KeyboardType.Number)
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.maxLikesControl, supportingText = "Write the maximum likes of the product", placeHolder = "Write a number...", label = "Product Maximum Likes", keyboardType = KeyboardType.Number)
    }
}

@Composable
private fun CategoryInformation(viewModel: SearchProductsViewModel) {
    val primaryCategories: State<List<String>> = viewModel.primaryCategories.collectAsState()
    val secondaryCategories: State<List<String>> = viewModel.secondaryCategories.collectAsState()
    val tertiaryCategories: State<List<String>> = viewModel.tertiaryCategories.collectAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Category Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Category information about the product, primary, secondary, tertiary categories", fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))

        FormDropdown(modifier = Modifier.padding(2.dp).fillMaxWidth(), label = "Primary Category", supportingText = "Please choose one of the available categories", valueCallback = {viewModel.updateSecondaries();viewModel.updateCurrentProducts(false)},formControl = viewModel.primaryCategoryControl, items = primaryCategories.value)
        FormDropdown(modifier = Modifier.padding(2.dp).fillMaxWidth(),supportingText = "Please choose one of the available secondary categories", valueCallback = {viewModel.updateTertiaries();viewModel.updateCurrentProducts(false)},formControl = viewModel.secondaryCategoryControl, items = secondaryCategories.value)
        FormDropdown(modifier = Modifier.padding(2.dp).fillMaxWidth(),label = "Tertiary Category", supportingText = "Please choose one of the available tertiary categories", valueCallback = {viewModel.updateCurrentProducts(false)},formControl = viewModel.tertiaryCategoryControl, items = tertiaryCategories.value)
    }
}

@Composable
private fun ItemsList(navController: NavHostController){
    val viewModel: SearchProductsViewModel = viewModel(factory = viewModelFactory)
    val currentProducts: State<List<Product>> = viewModel.currentProducts.collectAsState()
    val currentPage: State<Page> = viewModel.currentProductsPage.collectAsState()
    val isSearching: State<Boolean> = viewModel.currentProductsSearching.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage();
    }
    if(isSearching.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier.padding(5.dp)) {
            PageShower(page = currentPage.value)
            if(currentPage.value.totalElements > 0) {
                LazyVerticalGrid(state = lazyGridState,modifier = Modifier.padding(vertical = 2.dp), columns = GridCells.Fixed(2), verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.SpaceBetween, content = {
                    itemsIndexed(items = currentProducts.value) { _, item ->
                        Box(modifier = Modifier.padding(5.dp)) {
                            ProductCard(navController,product = item)
                        }
                    }
                })
            }
            else
                MissingItems(buttonText = "Reset search", callback = {viewModel.resetSearch()})
        }
    }
}