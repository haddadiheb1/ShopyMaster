package com.hanouti.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hanouti.app.viewmodel.ProductsViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
    onBack: () -> Unit
) {
    val products by viewModel.observeProducts().collectAsState(initial = emptyList())
    var editingBarcode by remember { mutableStateOf<String?>(null) }
    var editName by remember { mutableStateOf("") }
    var editPrice by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.products)) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }
        )
    }) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).padding(16.dp)) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(products) { p ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.name, style = MaterialTheme.typography.titleMedium)
                            Text(String.format("%s — %.2f", p.barcode, p.price), color = androidx.compose.ui.graphics.Color.Gray)
                        }
                        IconButton(onClick = {
                            editingBarcode = p.barcode
                            editName = p.name
                            editPrice = p.price.toString()
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { viewModel.deleteProduct(p.barcode) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }

    val currentBarcode = editingBarcode
    if (currentBarcode != null) {
        AlertDialog(
            onDismissRequest = { editingBarcode = null },
            title = { Text(stringResource(id = com.hanouti.app.R.string.edit_product)) },
            text = {
                Column {
                    Text(stringResource(id = com.hanouti.app.R.string.barcode_x, currentBarcode))
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.name)) })
                    OutlinedTextField(value = editPrice, onValueChange = { editPrice = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.price)) })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val price = editPrice.toDoubleOrNull() ?: 0.0
                    if (editName.isNotBlank() && price > 0.0) {
                        viewModel.updateProduct(currentBarcode, editName.trim(), price)
                        editingBarcode = null
                    }
                }) { Text(stringResource(id = com.hanouti.app.R.string.save)) }
            },
            dismissButton = {
                androidx.compose.material3.OutlinedButton(onClick = { editingBarcode = null }) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
            }
        )
    }
}


