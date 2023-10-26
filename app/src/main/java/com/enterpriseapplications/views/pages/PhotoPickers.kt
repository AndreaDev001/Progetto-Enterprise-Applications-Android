package com.enterpriseapplications.views.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun SinglePhotoPicker(visible: Boolean,callback: (item: Uri?) -> Unit) {
    val singlePhotoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(), onResult = {
        callback(it)
    })
    if(visible)
        singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}
@Composable
fun MultiPhotoPicker(visible: Boolean,callback: (item: List<Uri?>) -> Unit) {
    val multiPhotoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(), onResult = {
        callback(it)
    })
    if(visible)
        multiPhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}