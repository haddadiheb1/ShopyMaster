package com.hanouti.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hanouti.app.data.Client
import com.hanouti.app.data.CreditEntry
import com.hanouti.app.ui.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    client: Client?,
    onBack: () -> Unit,
    creditHistory: List<CreditEntry> = emptyList(),
    onApplyCreditChange: (deltaAmount: Double, note: String?) -> Unit = { _, _ -> },
    onDeleteClient: () -> Unit = {},
    onDeleteHistoryEntry: (CreditEntry) -> Unit = {},
    onEditHistoryEntry: (CreditEntry, Double, String?) -> Unit = { _, _, _ -> }
) {
    val title = client?.name ?: "Client"
    val creditColor = if ((client?.credit ?: 0.0) <= 0.0) Color(0xFF2E7D32) else Color(0xFFC62828)
    var entryToEdit by remember { mutableStateOf<CreditEntry?>(null) }

    Scaffold(
        topBar = {
            var showDeleteConfirm by remember { mutableStateOf(false) }
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = com.hanouti.app.R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(id = com.hanouti.app.R.string.delete_client))
                    }
                }
            )

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    title = { Text(stringResource(id = com.hanouti.app.R.string.delete_client)) },
                    text = { Text(stringResource(id = com.hanouti.app.R.string.delete_client_confirm)) },
                    confirmButton = {
                        Button(onClick = {
                            showDeleteConfirm = false
                            onDeleteClient()
                            onBack()
                        }) { Text(stringResource(id = com.hanouti.app.R.string.delete)) }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showDeleteConfirm = false }) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Name", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

            Text(text = "\nCredit", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = Formatters.amount(client?.credit ?: 0.0), color = creditColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            var showAddDialog by remember { mutableStateOf(false) }
            var showRemoveDialog by remember { mutableStateOf(false) }

            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(onClick = { showAddDialog = true }) { Text(stringResource(id = com.hanouti.app.R.string.add_amount)) }
                OutlinedButton(onClick = { showRemoveDialog = true }, modifier = Modifier.padding(start = 12.dp)) { Text(stringResource(id = com.hanouti.app.R.string.remove_amount)) }
            }

            if (showAddDialog) {
                AmountDialog(
                    title = "Add amount",
                    confirmText = "Add",
                    onDismiss = { showAddDialog = false },
                    onConfirm = { amount, note ->
                        if (amount > 0) onApplyCreditChange(amount, note)
                        showAddDialog = false
                    }
                )
            }

            if (showRemoveDialog) {
                AmountDialog(
                    title = "Remove amount",
                    confirmText = "Remove",
                    onDismiss = { showRemoveDialog = false },
                    onConfirm = { amount, note ->
                        if (amount > 0) onApplyCreditChange(-amount, note)
                        showRemoveDialog = false
                    }
                )
            }

            Text(text = "\nHistory", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            LazyColumn {
                items(creditHistory.sortedByDescending { it.timestampMillis }) { entry ->
                    val amountText = (if (entry.deltaAmount >= 0) "+" else "") + Formatters.amount(entry.deltaAmount)
                    val amountColor = if (entry.deltaAmount >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = amountText,
                                color = amountColor,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            val noteText = entry.note ?: ""
                            val dateText = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(entry.timestampMillis))
                            Text(text = listOfNotNull(noteText.ifBlank { null }, dateText).joinToString("  •  "), color = Color.Gray)
                        }
                        Row {
                            IconButton(onClick = { entryToEdit = entry }) {
                                Icon(Icons.Default.Edit, contentDescription = stringResource(id = com.hanouti.app.R.string.edit_entry))
                            }
                            IconButton(onClick = { onDeleteHistoryEntry(entry) }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(id = com.hanouti.app.R.string.delete))
                            }
                        }
                    }
                }
            }

            entryToEdit?.let { editable ->
                EditEntryDialog(
                    entry = editable,
                    onDismiss = { entryToEdit = null },
                    onConfirm = { newAmount, newNote ->
                        onEditHistoryEntry(editable, newAmount, newNote)
                        entryToEdit = null
                    }
                )
            }
        }
    }
}

@Composable
private fun AmountDialog(
    title: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (amount: Double, note: String?) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    val amount = amountText.toDoubleOrNull()
    val isValid = amount != null && amount > 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.amount)) })
                OutlinedTextField(value = noteText, onValueChange = { noteText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.note_optional)) }, modifier = Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Button(onClick = { amount?.let { onConfirm(it, noteText.ifBlank { null }) } }, enabled = isValid) {
                Text(confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
        }
    )
}

@Composable
private fun EditEntryDialog(
    entry: CreditEntry,
    onDismiss: () -> Unit,
    onConfirm: (newAmount: Double, newNote: String?) -> Unit
) {
    var amountText by remember { mutableStateOf(entry.deltaAmount.toString()) }
    var noteText by remember { mutableStateOf(entry.note ?: "") }
    val amount = amountText.toDoubleOrNull()
    val isValid = amount != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = com.hanouti.app.R.string.edit_entry)) },
        text = {
            Column {
                OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.amount)) })
                OutlinedTextField(value = noteText, onValueChange = { noteText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.note_optional)) }, modifier = Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Button(onClick = { amount?.let { onConfirm(it, noteText.ifBlank { null }) } }, enabled = isValid) {
                Text(stringResource(id = com.hanouti.app.R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
        }
    )
}
