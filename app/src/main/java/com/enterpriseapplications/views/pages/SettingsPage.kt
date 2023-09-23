package com.enterpriseapplications.views.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.enterpriseapplications.viewmodel.SettingsViewModel
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavHostController) {
    val viewModel: SettingsViewModel = SettingsViewModel()
    Column(modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            title = {
                Text(text = "Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }, navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            })
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            UserInformation(viewModel = viewModel)
        }
    }
}

@Composable
private fun UserInformation(viewModel: SettingsViewModel) {
    CustomTextField(modifier = Modifier.padding(5.dp).fillMaxWidth(),formControl = viewModel.nameControl, supportingText = "Write a new name", placeHolder = "Write a name...", label = "Name")
    CustomTextField(modifier = Modifier.padding(5.dp).fillMaxWidth(),formControl = viewModel.surnameControl, supportingText = "Write a new surname", placeHolder = "Write a name...", label = "Surname")
    CustomTextField(modifier = Modifier.padding(5.dp).fillMaxWidth(),formControl = viewModel.descriptionControl, supportingText = "Write a new description", placeHolder = "Write a description...", label = "Description")
    FormDropdown(modifier = Modifier.padding(5.dp).fillMaxWidth(),formControl = viewModel.genderControl, items = listOf("GENDER","MALE","NOT_SPECIFIED"), label = "Gender")
}