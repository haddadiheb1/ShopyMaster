package com.hanouti.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hanouti.app.data.SupplierBail
import com.hanouti.app.ui.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierBailsScreen(
    bails: List<SupplierBail>,
    onBack: () -> Unit,
    onAddBail: (name: String, amount: Double, photoUri: String?) -> Unit,
    onDeleteBail: (SupplierBail) -> Unit,
    onOpenBailDetail: (SupplierBail) -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = com.hanouti.app.R.string.supplier_bails_title)) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) { Icon(Icons.Default.Menu, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = { showAdd = true }) { Icon(Icons.Default.Add, contentDescription = null) }
                }
            )
        }
    ) { innerPadding ->
        val totalAmount = bails.sumOf { it.amount }
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxSize()) {
            item {
                ListItem(
                    headlineContent = { Text(stringResource(id = com.hanouti.app.R.string.total_bails_amount)) },
                    trailingContent = { Text(Formatters.amount(totalAmount)) }
                )
                Divider()
            }
            items(bails) { bail ->
                ListItem(
                    headlineContent = { Text(bail.supplierName) },
                    supportingContent = {
                        val dateText = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(bail.timestampMillis))
                        Text(text = Formatters.amount(bail.amount) + " • " + dateText)
                    },
                    leadingContent = {
                        if (!bail.photoUri.isNullOrBlank()) {
                            AsyncImage(model = Uri.parse(bail.photoUri), contentDescription = null, modifier = Modifier.size(48.dp))
                        }
                    },
                    trailingContent = {
                        IconButton(onClick = { onDeleteBail(bail) }) { Icon(Icons.Default.Delete, contentDescription = null) }
                    },
                    modifier = Modifier.fillMaxWidth().clickable { onOpenBailDetail(bail) }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        AddBailDialog(onDismiss = { showAdd = false }, onConfirm = { name, amount, photo ->
            onAddBail(name, amount, photo)
            showAdd = false
        })
    }
}

@Composable
private fun AddBailDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, amount: Double, photoUri: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    // Camera launcher with pre-created URI
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri = tempPhotoUri?.toString()
        }
    }

    fun createImageUri(context: Context): Uri? {
        return try {
            val imagesDir = java.io.File(context.cacheDir, "images").apply { mkdirs() }
            val file = java.io.File.createTempFile("bail_", ".jpg", imagesDir)
            FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        } catch (e: Exception) { null }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = com.hanouti.app.R.string.add_supplier_bail)) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.supplier_name)) }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.amount)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        val uri = createImageUri(context)
                        tempPhotoUri = uri
                        uri?.let { cameraLauncher.launch(it) }
                    }) { Text(stringResource(id = com.hanouti.app.R.string.take_photo)) }
                    Spacer(Modifier.width(12.dp))
                    OutlinedTextField(value = photoUri ?: "", onValueChange = { photoUri = it.ifBlank { null } }, label = { Text(stringResource(id = com.hanouti.app.R.string.photo_uri_optional)) }, modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            val amt = amountText.toDoubleOrNull()
            Button(onClick = { if (!name.isBlank() && amt != null && amt > 0) onConfirm(name.trim(), amt, photoUri) }, enabled = name.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) > 0.0) {
                Text(stringResource(id = com.hanouti.app.R.string.add))
            }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) } }
    )
}


