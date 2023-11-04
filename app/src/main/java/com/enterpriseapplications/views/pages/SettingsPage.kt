package com.enterpriseapplications.views.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.config.RetrofitConfig
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.viewmodel.SettingsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.FormDropdown
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavHostController) {
    val viewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
    Column(modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            title = {
                Text(text = "Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }, navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            })
        val currentAsyncUri: State<Uri?> = viewModel.currentSelectedUri.collectAsState()
        val photoPickerVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
        val successInfo: State<Boolean> = viewModel.successInfo.collectAsState()
        val successImages: State<Boolean> = viewModel.successImages.collectAsState()
        if(successInfo.value || successImages.value) {
            AlertDialog(icon = {
               Icon(imageVector = Icons.Filled.Person,contentDescription = null,modifier = Modifier.size(80.dp))
            }, onDismissRequest = {}, confirmButton = {
                Button(onClick = {
                    val path: String = "profilePage/" + AuthenticationManager.currentUser.value!!.userID;
                    navController.popBackStack()
                    navController.navigate(path)
                }, modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),shape = RoundedCornerShape(5.dp)) {
                    Text(text = "Go to user",modifier = Modifier.padding(2.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal)
                }
            }, text = {
                Column(modifier = Modifier.padding(2.dp)) {
                    Text(text = "Success", fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                    Text(text = "User updated correctly", fontSize = 15.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(2.dp))
                }
            })
        }
        val searching: State<Boolean> = viewModel.searching.collectAsState()
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.padding(vertical = 2.dp),text = "Here you can modify your profile, more information can help your profile to be more visible in searches")
            Spacer(modifier = Modifier.height(2.dp))
            if(searching.value) {
                ProgressIndicator()
            }
            else
            {
                Button(contentPadding = PaddingValues(0.dp),shape = RoundedCornerShape(60), onClick = { photoPickerVisible.value = true;
                }) {
                    SinglePhotoPicker(visible = photoPickerVisible.value, callback = {
                        viewModel.updateCurrentSelectedUri(it)
                        photoPickerVisible.value = false;
                    })
                    AsyncImage(modifier = Modifier.size(100.dp),contentScale = ContentScale.Crop, model = currentAsyncUri.value, contentDescription = null)
                }
                UserInformation(viewModel = viewModel)
                Spacer(modifier = Modifier.height(2.dp))
                ButtonSection(viewModel = viewModel)
            }
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
    val formValid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    val currentUri: State<Uri?> = viewModel.currentSelectedUri.collectAsState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Button(enabled = formValid.value || currentUri.value != null, modifier = Modifier.padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {viewModel.updateUser()}) {
            Text(text = "Confirm",fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Button(modifier = Modifier.padding(2.dp),shape = RoundedCornerShape(5.dp), onClick = {viewModel.reset()}) {
            Text(text = "Reset", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}