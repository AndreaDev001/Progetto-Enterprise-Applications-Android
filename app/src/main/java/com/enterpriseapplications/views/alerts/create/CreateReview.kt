package com.enterpriseapplications.views.alerts.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.viewmodel.create.CreateReviewViewModel
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.CustomTextField
import java.util.UUID

@Composable
fun CreateReview(userID: UUID,update: Boolean,confirmCallback: (createdReview: Review) -> Unit = {},cancelCallback: () -> Unit = {},dismissCallback: () -> Unit = {}) {
    val viewModel: CreateReviewViewModel = viewModel(factory = com.enterpriseapplications.viewmodel.viewModelFactory)
    val text: String = if(!update) "Create a Review" else "Update Review"
    val currentCreatedReview = viewModel.createdReview.collectAsState()
    val valid: State<Boolean> = viewModel.formGroup.valid.collectAsState()
    viewModel.reset()
    viewModel.userID = userID
    viewModel.update = update
    if(currentCreatedReview.value != null)
        confirmCallback(currentCreatedReview.value!!)
    AlertDialog(shape = RoundedCornerShape(10.dp), onDismissRequest = {dismissCallback()}, icon = {
        Icon(imageVector = Icons.Default.Reviews, contentDescription = null,modifier = Modifier.size(50.dp))
    }, text = {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "Please provide a the text and rating of the review",fontSize = 15.sp, fontWeight = FontWeight.Normal)
            CustomTextField(
                modifier = Modifier.padding(5.dp),
                formControl = viewModel.textControl,
                supportingText = "Write the text of the review",
                placeHolder = "Write a review...",
                label = "Text"
            )
            CustomTextField(
                modifier = Modifier.padding(5.dp),
                formControl = viewModel.ratingControl,
                supportingText = "Write the rating of the review",
                placeHolder = "Write a number...",
                label = "Rating",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(2.dp))
            CustomButton(enabled = valid.value, text = "Confirm", clickCallback = {viewModel.createReview()})
            CustomButton(text = "Cancel", clickCallback = {cancelCallback()})
        }
    }, confirmButton = {}, dismissButton = {})
}