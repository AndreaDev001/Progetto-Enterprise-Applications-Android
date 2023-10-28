package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.viewmodel.create.CreateAddressViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown

@Composable
fun CreateAddress(navController: NavHostController,confirmCallback: (address: Address) -> Unit = {},dismissCallback: () -> Unit = {},cancelCallback: () -> Unit = {}) {
    val viewModel: CreateAddressViewModel = viewModel(factory = viewModelFactory)
    viewModel.reset()
    val countries: State<List<String>> = viewModel.currentCountries.collectAsState()
    val currentCandidates: State<List<String>> = viewModel.currentCandidates.collectAsState()
    val currentCandidatesSearching: State<Boolean> = viewModel.currentCountriesSearching.collectAsState()
    val queryVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
    val createdAddress: State<Address?> = viewModel.createdAddress.collectAsState()
    AlertDialog(shape = RoundedCornerShape(10.dp),onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null,modifier = Modifier.size(50.dp))
    }, title = {
        Text(text = "Create Address", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = {
        Column(modifier = Modifier
            .padding(2.dp)
            .verticalScroll(ScrollState(0))) {
            Text(modifier = Modifier.padding(2.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal, text = "Choose one of the countries available addresses, query the desired address, then choose one of the found addresses")
            FormDropdown(valueCallback = {
                queryVisible.value = it != "";
            }, modifier = Modifier.padding(2.dp),formControl = viewModel.countryCodeControl, items = countries.value)
            CustomTextField(label = "Address", placeHolder = "Write an address...", supportingText = "Write the street name, then use dropdown below to choose one of the found addresses", valueCallback = {viewModel.searchForAddresses();viewModel.resetSearch()}, enabled = queryVisible.value, modifier = Modifier.padding(5.dp),formControl = viewModel.queryControl, keyboardType = KeyboardType.Text)
            CustomTextField(label = "Owner Name", placeHolder = "Owner's name...", supportingText = "Write the name of owner of this address", modifier = Modifier.padding(5.dp),formControl = viewModel.ownerNameControl)
            FormDropdown(searching = currentCandidatesSearching.value, supportingText = "Choose one of the available options", valueCallback = {
                viewModel.updateSelectedStreet(it)
            }, label = "Street", modifier = Modifier.padding(2.dp),formControl = viewModel.selectedStreetControl, items = currentCandidates.value)
            CustomTextField(modifier = Modifier.padding(2.dp),label = "Street", editable = false, formControl = viewModel.streetControl)
            CustomTextField(label = "Locality",editable = false, modifier = Modifier.padding(2.dp),formControl = viewModel.localityControl)
            CustomTextField(label = "Postal Code", editable = false, modifier = Modifier.padding(2.dp),formControl = viewModel.postalCodeControl)
        }
        if(createdAddress.value != null)
            confirmCallback(createdAddress.value!!)
    }, confirmButton = {
        Button(onClick = {
            viewModel.createAddress()}
        ) {
            Text(text = "Confirm", fontSize = 15.sp)
        }
    }, dismissButton = {
        Button(onClick = {cancelCallback()}) {
            Text(text = "Cancel",fontSize = 15.sp)
        }
    })
}