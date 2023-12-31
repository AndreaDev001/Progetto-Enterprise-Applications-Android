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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.navigation.Destination
import com.enterpriseapplications.navigation.Screen
import com.enterpriseapplications.viewmodel.search.SearchReportsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ReportCard
import com.enterpriseapplications.views.alerts.create.CreateBan
import com.enterpriseapplications.views.lists.MenuItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchReports(navController: NavHostController) {
    val viewModel: SearchReportsViewModel = viewModel(factory = viewModelFactory)
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp)
    ) {
        TopAppBar(title = {
            Text(text = "Search Reports", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        val controller: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current;
        val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
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
                                        controller?.hide();
                                        drawerState.close()
                                    }
                                },
                                trailingIcon = Icons.Filled.Close,
                                headerText = "Filters",
                                supportingText = "Use the following filters to find the desired reports"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FilterOptions()
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
                        Text(modifier = Modifier.padding(2.dp),text = "Filters", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                    }
                    Text(modifier = Modifier.padding(2.dp),text = "Use the available filters the find the desired reports",fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    Spacer(modifier = Modifier.height(2.dp))
                    ItemList(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun FilterOptions() {
    val viewModel: SearchReportsViewModel = viewModel(factory = viewModelFactory)
    val reasons: State<List<String>> = viewModel.reasons.collectAsState()
    val types: State<List<String>> = viewModel.types.collectAsState()
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.reporterEmail, supportingText = "Write the reporter's email", placeHolder = "Write an email...", label = "Reporter Email")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.reportedEmail, supportingText = "Write the reported email", placeHolder = "Write an email...", label = "Reported Email")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.reporterUsername, supportingText = "Write the reporter username", placeHolder = "Write an username...", label = "Reporter username")
        CustomTextField(modifier = Modifier.padding(2.dp),valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.reportedUsername, supportingText = "Write the reported username", placeHolder = "Write an username...",label = "Reported Username")
        FormDropdown(modifier = Modifier.padding(2.dp),valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.reason, items = reasons.value, label = "Reason", supportingText = "Please choose one of the available options")
        FormDropdown(modifier = Modifier.padding(2.dp),valueCallback = {viewModel.updateCurrentReports(false)},formControl = viewModel.type, items = types.value, label = "Type", supportingText = "Please choose one of the available options")
    }
}
@Composable
private fun ItemList(navController: NavHostController) {
    val viewModel: SearchReportsViewModel = viewModel(factory = viewModelFactory)
    val currentReports: State<List<Report>> = viewModel.currentReports.collectAsState()
    val currentPage: State<Page> = viewModel.currentReportsPage.collectAsState()
    val currentReportsSearching: State<Boolean> = viewModel.currentReportsSearching.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val currentReport: MutableState<Report?> = remember { mutableStateOf(null) }
    val callback: () -> Unit = {currentReport.value = null;}
    if(currentReport.value != null)
        CreateBan(currentReport.value, update = false, confirmCallback = {
            callback()
            navController.navigate(Destination.Search.SearchBans.route)
        }, cancelCallback = callback, dismissCallback = callback)
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage()
    }
    if(currentReportsSearching.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier.padding(5.dp)) {
            PageShower(page = currentPage.value)
            if(currentPage.value.totalElements > 0) {
                LazyVerticalGrid(state = lazyGridState,modifier = Modifier.padding(vertical = 2.dp), columns = GridCells.Fixed(2), verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.SpaceBetween, content = {
                    itemsIndexed(items = currentReports.value) { _, item ->
                        Box(modifier = Modifier.padding(5.dp)) {
                            ReportCard(report = item, clickCallback = {currentReport.value = item})
                        }
                    }
                })
            }
            else
                MissingItems(buttonText = "Reset search", callback = {viewModel.resetSearch()})
        }
    }
}