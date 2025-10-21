package com.hanouti.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    onSave: (name: String, credit: Double) -> Unit,
    onCancel: () -> Unit
) {
    val (name, setName) = remember { mutableStateOf("") }
    val (creditText, setCreditText) = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = com.hanouti.app.R.string.add_client_title)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = com.hanouti.app.R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = setName,
                label = { Text(stringResource(id = com.hanouti.app.R.string.name_label)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = creditText,
                onValueChange = setCreditText,
                label = { Text(stringResource(id = com.hanouti.app.R.string.credit_balance)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val credit = creditText.toDoubleOrNull() ?: 0.0
                    onSave(name.trim(), credit)
                }, enabled = name.isNotBlank()) {
                    Text(stringResource(id = com.hanouti.app.R.string.save))
                }
                OutlinedButton(onClick = onCancel) {
                    Text(stringResource(id = com.hanouti.app.R.string.cancel))
                }
            }
        }
    }
}
