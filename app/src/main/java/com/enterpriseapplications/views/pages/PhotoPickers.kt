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