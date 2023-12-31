package com.enterpriseapplications.views.pages.search

import android.graphics.pdf.PdfDocument.Page
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.views.ProductCard

@Composable
fun MissingItems(iconSize: Dp = 80.dp, missingText: String = "No results found, set is empty",useButton: Boolean = true, missingIcon: ImageVector = Icons.Filled.Search,callback: () -> Unit, buttonText: String = "Retry", button: @Composable () ->  Unit = {
    Button(onClick = {callback()}, shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(vertical = 10.dp)) {
        Text(text = buttonText,modifier = Modifier.padding(5.dp), fontSize = 15.sp)
    }
})
{
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = missingIcon,contentDescription = null,modifier = Modifier
            .padding(5.dp)
            .size(iconSize))
        Text(text = missingText, fontSize = 15.sp,modifier = Modifier.padding(5.dp))
        if(useButton)
            button();
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdown(modifier: Modifier = Modifier,useMissingButton: Boolean = false,missingCallback: () -> Unit = {},searching: Boolean = false,supportingText: String? = null,formControl: FormControl<String?>,label: String? = null,items: List<String>,valueCallback: (item: String) -> Unit = {},expandedChange: (value: Boolean) -> Unit = {}) {
    val expanded: MutableState<Boolean> = remember {mutableStateOf(false)};
    val isValid = formControl.valid.collectAsState()
    val value: State<String?> = formControl.currentValue.collectAsState()
    val touched: State<Boolean> = formControl.touched.collectAsState()
    val currentText: String = if(value.value != null) value.value!! else "";
    if(searching)
        ProgressIndicator(size = 20.dp,strokeWidth = 1.dp)
    else
    {
        if(items.isNotEmpty()) {
            ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = !expanded.value;expandedChange(expanded.value)}) {
                TextField(readOnly = true, modifier = modifier.menuAnchor(),value = currentText, onValueChange = {valueCallback(it)}, trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )},label = {
                    if(label != null)
                        Text(text = label, fontSize = 15.sp)
                },supportingText = {
                    if(supportingText != null)
                        Text(text = supportingText, fontSize = 15.sp, fontWeight = FontWeight.Thin)
                }, isError = !isValid.value && touched.value)
                ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {expanded.value = false}) {
                    items.forEach { value: String ->
                        DropdownMenuItem(text = {
                            Text(text = value, fontSize = 15.sp)
                        }, onClick = {
                            expanded.value = false
                            formControl.updateValue(value)
                            valueCallback(value);
                        })
                    }
                }
            }
        }
        else
            MissingItems(iconSize = 20.dp,callback = missingCallback, useButton = useMissingButton);
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(modifier: Modifier = Modifier,editable: Boolean = true,enabled: Boolean = true, formControl: FormControl<String?>, leadingIcon: ImageVector? = null, trailingIcon: ImageVector? = null, label: String? = null, supportingText: String? = null, placeHolder: String? = null, keyboardType: KeyboardType = KeyboardType.Text, valueCallback: (item: String) -> Unit = {}) {
    val currentValue = formControl.currentValue.collectAsState()
    val currentErrors = formControl.errorsText.collectAsState()
    val isValid: State<Boolean>  = formControl.valid.collectAsState()
    val touched: State<Boolean> = formControl.touched.collectAsState()
    val currentText: String = if(currentValue.value != null) currentValue.value!! else "";
    TextField(readOnly =!editable, enabled = enabled,textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),modifier = modifier,value = currentText, onValueChange = {
        formControl.updateValue(it)
        valueCallback(it)
    }, isError = !isValid.value && touched.value
    , leadingIcon = {
        if(leadingIcon != null)
            Icon(imageVector = leadingIcon, contentDescription = null,modifier = Modifier.size(30.dp))
        }
    , trailingIcon = {
        if(trailingIcon != null)
            Icon(imageVector = trailingIcon, contentDescription = null, modifier = Modifier.size(30.dp))
        },label = {
            if(label != null)
                Text(text = label, fontSize = 15.sp)
        }, placeholder = {
             if(placeHolder != null)
                 Text(text = placeHolder, fontSize = 15.sp)
        }, supportingText = {
            if(supportingText != null && (isValid.value || !touched.value))
                Text(text = supportingText, fontSize = 15.sp, fontWeight = FontWeight.Thin)
            else
            {
                Column()
                {
                    if(touched.value) {
                        for (currentError in currentErrors.value) {
                            Text(text = currentError, fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth())
                        }
                    }
                }
            }
        }, keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}
@Composable
fun ProgressIndicator(size: Dp = 40.dp,text: String? = "Searching",strokeWidth: Dp = 4.dp) {
    Row(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(modifier = Modifier.size(size), strokeWidth = strokeWidth)
            if(text != null)
                Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}
@Composable
fun SearchingDialog(dismissCallback: () -> Unit = {}) {
    Dialog(
        onDismissRequest = { dismissCallback() },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier.background(White, shape = RoundedCornerShape(8.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
                Text(text = "Please Wait...", fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
@Composable
fun ProductList(navController: NavHostController, scrolledCallback: () -> Unit = {}, currentPage: com.enterpriseapplications.model.Page? = null, currentItems: List<Product>, searching: Boolean = false, vertical: Boolean = true) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        if(searching)
            ProgressIndicator()
        else
        {
            Column(modifier = Modifier.padding(5.dp)) {
                if(currentPage != null) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        Text(text = "Use the available filters to find the desired products", fontSize = 18.sp,modifier = Modifier.padding(vertical = 2.dp))
                        Text(text = "${currentPage.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
                        Text(text = "${currentPage.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
                        Text(text = "${currentPage.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
                if(currentItems.isNotEmpty()) {
                    if(vertical) {
                        val lazyGridState: LazyGridState = rememberLazyGridState()
                        val bottomReached by remember {
                            derivedStateOf {
                                lazyGridState.isScrolledToEnd()
                            }
                        }
                        LaunchedEffect(bottomReached) {
                            scrolledCallback()
                        }
                        LazyVerticalGrid(columns = GridCells.Fixed(2),state = lazyGridState) {
                            itemsIndexed(currentItems) {index,item ->
                                Box(modifier = Modifier.padding(2.dp)) {
                                    ProductCard(navController,item)
                                }
                            }
                        }
                    }
                    else
                    {
                        val lazyRowState = rememberLazyListState()
                        val endReached by remember {
                            derivedStateOf {
                                lazyRowState.isScrolledToEnd()
                            }
                        }
                        LaunchedEffect(endReached) {
                            scrolledCallback()
                        }
                        LazyRow(state = lazyRowState) {
                            itemsIndexed(currentItems) { index, item ->
                                Box(modifier = Modifier.padding(2.dp)) {
                                    ProductCard(navController,item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PageShower(modifier: Modifier = Modifier,page: com.enterpriseapplications.model.Page?) {
    if(page != null) {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(text = "${page.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${page.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}
@Composable
fun CustomButton(modifier: Modifier = Modifier,enabled: Boolean = true,text: String,clickCallback: () -> Unit = {}) {
    Button(enabled = enabled,modifier = modifier.padding(5.dp).fillMaxWidth(),shape = RoundedCornerShape(5.dp), onClick = {clickCallback()}) {
        Text(text = text,fontSize = 15.sp, fontWeight = FontWeight.Normal)
    }
}