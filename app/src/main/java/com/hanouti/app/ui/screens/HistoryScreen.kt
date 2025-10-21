package com.hanouti.app.ui.screens
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.hanouti.app.viewmodel.HistoryViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(currentLocalDateString()) }
    val dailyTotals by viewModel.observeDailyTotals().collectAsState(initial = emptyList())
    val salesForDate by viewModel.observeSalesForDate(selectedDate).collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.history_title)) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }
        )
    }) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).padding(16.dp)) {
            OutlinedTextField(value = selectedDate, onValueChange = { selectedDate = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.date_yyyy_mm_dd)) }, modifier = Modifier.fillMaxWidth())
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val exportResult = remember { mutableStateOf<String?>(null) }
            Button(
                onClick = {
                    exportResult.value = null
                    coroutineScope.launch {
                        val sales = salesForDate
                        val saleItems = mutableMapOf<Int, List<com.hanouti.app.data.SaleItem>>()
                        for (sale in sales) {
                            val items = viewModel.observeItemsForSale(sale.id).first()
                            saleItems[sale.id] = items
                        }
                        val file = com.hanouti.app.util.CsvExporter.exportSalesHistory(
                            context,
                            sales,
                            saleItems,
                            "Hanouti_SalesHistory_${selectedDate}.csv"
                        )
                        exportResult.value = if (file != null) "Exported to ${file.absolutePath}" else "Export failed"
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
            Text(stringResource(id = com.hanouti.app.R.string.export_sales_csv))
            }
            exportResult.value?.let { Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 4.dp)) }
            Text(stringResource(id = com.hanouti.app.R.string.daily_totals))
            LazyColumn(Modifier.weight(1f)) {
                items(dailyTotals) { row ->
                    Text("${row.day} — ${String.format("%.2f", row.total)}")
                }
            }
            Divider()
            Text(stringResource(id = com.hanouti.app.R.string.sales_for_date, selectedDate))
            LazyColumn(Modifier.weight(1f)) {
                items(salesForDate) { sale ->
                    Text("${java.text.SimpleDateFormat("HH:mm").format(java.util.Date(sale.timestampMillis))} — ${String.format("%.2f", sale.totalAmount)}")
                }
            }
        }
    }
}

private fun currentLocalDateString(): String {
    val df = java.text.SimpleDateFormat("yyyy-MM-dd")
    return df.format(java.util.Date())
}


