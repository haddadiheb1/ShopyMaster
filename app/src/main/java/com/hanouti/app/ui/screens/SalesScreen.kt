package com.hanouti.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hanouti.app.viewmodel.SalesViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.shape.RoundedCornerShape
import com.hanouti.app.ui.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    viewModel: SalesViewModel,
    onBack: () -> Unit,
    onOpenScanner: () -> Unit,
    onFinished: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            var showManualDialog by remember { mutableStateOf(false) }
            var manualName by remember { mutableStateOf("") }
            var manualPriceText by remember { mutableStateOf("") }
            TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.new_purchase)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                actions = {
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) { Icon(Icons.Default.PlaylistAdd, contentDescription = stringResource(id = com.hanouti.app.R.string.add)) }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = com.hanouti.app.R.string.add_amount)) },
                            onClick = {
                                menuExpanded = false
                                showManualDialog = true
                            }
                        )
                    }
                    if (showManualDialog) {
                        AlertDialog(
                            onDismissRequest = { showManualDialog = false },
                            title = { Text(stringResource(id = com.hanouti.app.R.string.add_amount)) },
                            text = {
                                Column(Modifier.fillMaxWidth()) {
                                    OutlinedTextField(value = manualPriceText, onValueChange = { manualPriceText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.price)) }, modifier = Modifier.fillMaxWidth())
                                }
                            },
                            confirmButton = {
                                val price = manualPriceText.toDoubleOrNull() ?: 0.0
                                Button(onClick = {
                                    if (price > 0.0) {
                                        viewModel.addManualAmount(price)
                                        manualPriceText = ""
                                        showManualDialog = false
                                    }
                                }) { Text(stringResource(id = com.hanouti.app.R.string.add)) }
                            },
                            dismissButton = {
                                OutlinedButton(onClick = { showManualDialog = false }) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenScanner) { Icon(Icons.Default.Add, contentDescription = stringResource(id = com.hanouti.app.R.string.scan_product)) }
        }
    ) { inner ->
        var isSaving by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxSize().padding(inner).padding(16.dp)) {
            LazyColumn(Modifier.weight(1f)) {
                items(viewModel.lines) { line ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(line.product.name, style = MaterialTheme.typography.titleMedium)
                            Text("${line.quantity} × ${Formatters.amount(line.product.price)}", color = androidx.compose.ui.graphics.Color.Gray)
                        }
                            // Quantity controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.decrementProduct(line.product.barcode) }) {
                                    Text("-", fontWeight = FontWeight.Bold)
                                }
                                Text(line.quantity.toString(), modifier = Modifier.padding(horizontal = 4.dp))
                                IconButton(onClick = { viewModel.addProduct(line.product) }) {
                                    Text("+", fontWeight = FontWeight.Bold)
                                }
                                IconButton(onClick = {
                                    viewModel.removeProduct(line.product.barcode)
                                }) { Icon(Icons.Default.Delete, contentDescription = stringResource(id = com.hanouti.app.R.string.remove)) }
                            }
                            Text(Formatters.amount(line.product.price * line.quantity), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(Formatters.amount(viewModel.total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    isSaving = true
                    viewModel.finishTransaction { id ->
                        isSaving = false
                        onFinished(id)
                    }
                },
                enabled = viewModel.lines.isNotEmpty() && !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(56.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Text(stringResource(id = com.hanouti.app.R.string.finish_transaction))
                    }
                }
            }
        }
    }
}


