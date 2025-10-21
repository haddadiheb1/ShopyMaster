package com.hanouti.app.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hanouti.app.data.SupplierBail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierBailDetailScreen(
    bail: SupplierBail?,
    onBack: () -> Unit,
    onSave: (SupplierBail) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bail?.supplierName ?: "Bail") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            if (bail != null) {
                var name by remember { mutableStateOf(bail.supplierName) }
                var amountText by remember { mutableStateOf(String.format("%.2f", bail.amount)) }

                if (!bail.photoUri.isNullOrBlank()) {
                    AsyncImage(model = Uri.parse(bail.photoUri), contentDescription = null, modifier = Modifier.fillMaxWidth().height(240.dp))
                    Spacer(Modifier.height(12.dp))
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Supplier name") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text("Amount") }, keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    val amount = amountText.toDoubleOrNull() ?: bail.amount
                    onSave(bail.copy(supplierName = name.trim(), amount = amount))
                    onBack()
                }, enabled = name.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) > 0.0) {
                    Text("Save changes")
                }
            }
        }
    }
}


