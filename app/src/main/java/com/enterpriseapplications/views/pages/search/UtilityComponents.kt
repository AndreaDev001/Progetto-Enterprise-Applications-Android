package com.enterpriseapplications.views.pages.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enterpriseapplications.form.FormControl

@Composable
fun MissingItems(missingText: String = "No results found, set is empty", missingIcon: ImageVector = Icons.Filled.Search,callback: () -> Unit, buttonText: String = "Retry", button: @Composable () ->  Unit = {
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
            .size(80.dp))
        Text(text = missingText, fontSize = 15.sp,modifier = Modifier.padding(5.dp))
        button();
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdown(modifier: Modifier = Modifier,formControl: FormControl<String?>,label: String? = null,items: List<String>,valueCallback: () -> Unit = {},expandedChange: (value: Boolean) -> Unit = {}) {
    var expanded: MutableState<Boolean> = remember {mutableStateOf(false)};
    val value: State<String?> = formControl.currentValue.collectAsState()
    if(items.isNotEmpty()) {
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = !expanded.value;expandedChange(expanded.value)}) {
            TextField(readOnly = true, modifier = modifier.menuAnchor(),value = value.value.toString(), onValueChange = {valueCallback()}, trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded.value
            )},label = {
                if(label != null)
                    Text(text = label, fontSize = 15.sp)
            })
            ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {expanded.value = false}) {
                items.forEach { value: String ->
                    DropdownMenuItem(text = {
                       Text(text = value, fontSize = 15.sp)
                    }, onClick = {
                        expanded.value = false
                        formControl.updateValue(value)
                        valueCallback();
                    })
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(modifier: Modifier = Modifier, formControl: FormControl<String?>, leadingIcon: ImageVector? = null, trailingIcon: ImageVector? = null, label: String? = null, supportingText: String? = null, placeHolder: String? = null, keyboardType: KeyboardType = KeyboardType.Text, valueCallback: () -> Unit = {}) {
    val currentValue = formControl.currentValue.collectAsState()
    val currentErrors = formControl.errors.collectAsState()
    val isValid = formControl.valid.collectAsState()
    TextField(modifier = modifier,value = currentValue.value.toString(), onValueChange = {
        formControl.updateValue(it)
        valueCallback()
    }, isError = !isValid.value
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
            if(supportingText != null && isValid.value)
                Text(text = supportingText, fontSize = 15.sp)
            else
            {
                Column()
                {
                    for (s in currentErrors.value) {
                        Text(text = s, fontSize = 15.sp)
                    }
                }
            }
        }, keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}