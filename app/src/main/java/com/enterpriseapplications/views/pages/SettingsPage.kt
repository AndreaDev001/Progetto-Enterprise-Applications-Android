package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.viewmodel.SettingsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavHostController) {
    val viewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    viewModel.userID = authenticatedUser.value!!.userID
    Column(modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            title = {
                Text(text = "Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }, navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            })
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.padding(vertical = 2.dp),text = "Here you can modify your profile, more information can help your profile to be more visible in searches")
            Spacer(modifier = Modifier.height(2.dp))
            UserInformation(viewModel = viewModel)
            Spacer(modifier = Modifier.height(2.dp))
            ButtonSection(viewModel = viewModel)
        }
    }
}

@Composable
private fun UserInformation(viewModel: SettingsViewModel) {
    val genders: State<List<String>> = viewModel.genders.collectAsState()
    val visibilities: State<List<String>> = viewModel.visibilities.collectAsState()
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.nameControl, supportingText = "Write a new name", placeHolder = "Write a name...", label = "Name")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.surnameControl, supportingText = "Write a new surname", placeHolder = "Write a name...", label = "Surname")
    CustomTextField(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.descriptionControl, supportingText = "Write a new description", placeHolder = "Write a description...", label = "Description")
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.genderControl, items = genders.value, label = "Gender")
    FormDropdown(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),formControl = viewModel.visibilityControl , items = visibilities.value,label = "Visibility")
}
@Composable
private fun ButtonSection(viewModel: SettingsViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Button(modifier = Modifier.padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {viewModel.updateUser()}) {
            Text(text = "Confirm",fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Button(modifier = Modifier.padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {viewModel.reset()}) {
            Text(text = "Reset", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}