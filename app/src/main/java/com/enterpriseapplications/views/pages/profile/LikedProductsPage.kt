package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedProductsPage(navController: NavHostController)
{
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Liked Products", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        var state by remember { mutableStateOf(0)}
        val titles = listOf("Liked Products","Likes received")
        val icons = listOf(Icons.Filled.ThumbUp,Icons.Filled.Person)
        TabRow(selectedTabIndex = state,modifier = Modifier.fillMaxWidth()) {
            titles.forEachIndexed {index,currentTitle ->
                Tab(selected = index == state, onClick = {state = index},text = {
                    Text(text = currentTitle)
                }, icon = {Icon(imageVector = icons[index], contentDescription = null)})
            }
        }
    }
}