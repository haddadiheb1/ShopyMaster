package com.hanouti.app.ui.screens
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.History
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.hanouti.app.data.Client
import com.hanouti.app.data.Product
import com.hanouti.app.data.CreditEntry
import com.hanouti.app.data.SaleTransaction
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun DashboardScreen(
    totalClients: Int,
    totalProducts: Int,
    totalSales: Int,
    totalCredit: Double,
    todaySales: Double,
    totalBailsAmount: Double,
    topClients: List<Client>,
    topProducts: List<Product>,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onDateRangeChange: ((Long, Long) -> Unit)? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var startDateMillis by remember { mutableStateOf<Long?>(null) }
    var endDateMillis by remember { mutableStateOf<Long?>(null) }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val scrollState = rememberScrollState()
    
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with back button
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = com.hanouti.app.R.string.back))
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Dashboard",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            // Date filter UI
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Date Range Filter", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Start: " + (startDateMillis?.let { dateFormat.format(Date(it)) } ?: "-"),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "End: " + (endDateMillis?.let { dateFormat.format(Date(it)) } ?: "-"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { showDatePicker = true }) {
                        Text(stringResource(id = com.hanouti.app.R.string.select_dates))
                    }
                }
            }
            
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
            
            if (isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Statistics Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardStatCard(
                        title = "Clients", 
                        value = "$totalClients", 
                        icon = Icons.Default.People, 
                        color = MaterialTheme.colorScheme.primary, 
                        modifier = Modifier.weight(1f)
                    )
                    DashboardStatCard(
                        title = "Products", 
                        value = "$totalProducts", 
                        icon = Icons.Default.Inventory2, 
                        color = MaterialTheme.colorScheme.secondary, 
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardStatCard(
                        title = "Sales", 
                        value = "$totalSales", 
                        icon = Icons.Default.ShoppingCart, 
                        color = MaterialTheme.colorScheme.tertiary, 
                        modifier = Modifier.weight(1f)
                    )
                    DashboardStatCard(
                        title = "Credit", 
                        value = "${String.format("%.2f", totalCredit)}",
                        icon = Icons.Default.ReceiptLong, 
                        color = MaterialTheme.colorScheme.error, 
                        modifier = Modifier.weight(1f)
                    )
                }
                
                DashboardStatCard(
                    title = "Bails", 
                    value = "${String.format("%.2f", totalBailsAmount)}",
                    icon = Icons.Default.ReceiptLong, 
                    color = MaterialTheme.colorScheme.secondary, 
                    modifier = Modifier.fillMaxWidth()
                )
                
                DashboardStatCard(
                    title = "Today's Sales", 
                    value = "${String.format("%.2f", todaySales)}",
                    icon = Icons.Default.History, 
                    color = MaterialTheme.colorScheme.primary, 
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                
                // Top Clients Section
                Text("Top Clients", style = MaterialTheme.typography.titleMedium)
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        if (topClients.isEmpty()) {
                            Text(stringResource(id = com.hanouti.app.R.string.no_clients_yet))
                        } else {
                            topClients.forEach { client ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(client.name, fontWeight = FontWeight.Bold)
                                    Text("Credit: ${String.format("%.2f", client.credit)}", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(8.dp))
                
                // Top Products Section
                Text("Top Products", style = MaterialTheme.typography.titleMedium)
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        if (topProducts.isEmpty()) {
                            Text(stringResource(id = com.hanouti.app.R.string.no_products_yet))
                        } else {
                            topProducts.forEach { product ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(product.name, fontWeight = FontWeight.Bold)
                                    Text("Price: ${String.format("%.2f", product.price)}", color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
            }

            // Date picker dialog
            if (showDatePicker) {
                DateRangePickerDialog(
                    initialStartMillis = startDateMillis,
                    initialEndMillis = endDateMillis,
                    onDismiss = { showDatePicker = false },
                    onDateRangeSelected = { start, end ->
                        showDatePicker = false
                        if (start > end) {
                            // Invalid range
                            if (onDateRangeChange != null) onDateRangeChange(0L, 0L)
                        } else {
                            startDateMillis = start
                            endDateMillis = end
                            if (onDateRangeChange != null) onDateRangeChange(start, end)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DateRangePickerDialog(
    initialStartMillis: Long?,
    initialEndMillis: Long?,
    onDismiss: () -> Unit,
    onDateRangeSelected: (Long, Long) -> Unit
) {
    // Use Material3 DatePicker for start and end
    var tempStartMillis by remember { mutableStateOf(initialStartMillis ?: System.currentTimeMillis()) }
    var tempEndMillis by remember { mutableStateOf(initialEndMillis ?: System.currentTimeMillis()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onDateRangeSelected(tempStartMillis, tempEndMillis) }) {
                Text(stringResource(id = com.hanouti.app.R.string.apply))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) }
        },
        title = { Text(stringResource(id = com.hanouti.app.R.string.select_date_range)) },
        text = {
            Column {
                Text(stringResource(id = com.hanouti.app.R.string.start_date))
                DatePickerField(tempStartMillis) { tempStartMillis = it }
                Spacer(Modifier.height(8.dp))
                Text(stringResource(id = com.hanouti.app.R.string.end_date))
                DatePickerField(tempEndMillis) { tempEndMillis = it }
            }
        }
    )
}

@Composable
fun DatePickerField(selectedMillis: Long, onDateSelected: (Long) -> Unit) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    var showPicker by remember { mutableStateOf(false) }
    OutlinedButton(onClick = { showPicker = true }) {
        Text(dateFormat.format(Date(selectedMillis)))
    }
    if (showPicker) {
        // Use AndroidView for native DatePickerDialog
        AndroidDatePickerDialog(
            initialMillis = selectedMillis,
            onDateSelected = {
                onDateSelected(it)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
    }
}

@Composable
fun AndroidDatePickerDialog(
    initialMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val calendar = Calendar.getInstance().apply { timeInMillis = initialMillis }
        val dialog = android.app.DatePickerDialog(
            context,
            { _, y, m, d ->
                val cal = Calendar.getInstance()
                cal.set(y, m, d)
                onDateSelected(cal.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.setOnCancelListener { onDismiss() }
        dialog.setOnDismissListener { onDismiss() }
        dialog.show()
        onDispose { dialog.dismiss() }
    }
}

@Composable
fun DashboardStatCard(
    title: String, 
    value: String, 
    icon: ImageVector, 
    color: Color, 
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(32.dp))
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}
