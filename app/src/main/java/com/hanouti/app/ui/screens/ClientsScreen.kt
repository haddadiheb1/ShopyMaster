package com.hanouti.app.ui.screens
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.hanouti.app.data.Client
import com.hanouti.app.ui.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    clients: List<Client>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onClientClick: (Int) -> Unit,
    onOpenSupplierBails: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
        onOpenSales: () -> Unit = {},
        onGetCreditHistory: suspend (Int) -> List<com.hanouti.app.data.CreditEntry>
) {
    val filtered = clients.filter { client ->
        val q = searchQuery.trim().lowercase()
        q.isEmpty() || client.name.lowercase().contains(q)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Clients") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(id = com.hanouti.app.R.string.menu))
                    }
                },
                actions = { /* No Scan button in top bar */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = com.hanouti.app.R.string.add_client))
            }
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
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text(stringResource(id = com.hanouti.app.R.string.search_clients)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val exportResult = remember { mutableStateOf<String?>(null) }
            Button(
                onClick = {
                    exportResult.value = null
                    coroutineScope.launch {
                        val credits = mutableMapOf<Int, List<com.hanouti.app.data.CreditEntry>>()
                        for (client in clients) {
                            val history = onGetCreditHistory(client.id)
                            credits[client.id] = history
                        }
                        val file = com.hanouti.app.util.CsvExporter.exportClientsCredits(
                            context,
                            clients,
                            credits,
                            "Hanouti_ClientCredits.csv"
                        )
                        exportResult.value = if (file != null) "Exported to ${file.absolutePath}" else "Export failed"
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(stringResource(id = com.hanouti.app.R.string.export_credits_csv))
            }
            exportResult.value?.let { Text(it, color = Color.Green, modifier = Modifier.padding(vertical = 4.dp)) }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered) { client ->
                    ClientRow(client = client, onClick = { onClientClick(client.id) })
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ClientRow(client: Client, onClick: () -> Unit) {
    val creditColor = if (client.credit <= 0.0) Color(0xFF2E7D32) else Color(0xFFC62828)
    ListItem(
        headlineContent = {
            Text(text = client.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        },
        supportingContent = {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Credit:")
                Text(text = Formatters.amount(client.credit), color = creditColor, fontWeight = FontWeight.Bold)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
}
