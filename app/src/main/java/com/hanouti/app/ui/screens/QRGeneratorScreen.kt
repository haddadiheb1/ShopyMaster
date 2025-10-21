package com.hanouti.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRGeneratorScreen(
    onBack: () -> Unit,
    onGenerateQR: () -> String
) {
    var qrBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var databaseStats by remember { mutableStateOf<com.hanouti.app.data.DatabaseStats?>(null) }
    var lastGeneratedTime by remember { mutableStateOf<Long?>(null) }
    
    val context = LocalContext.current
    
    // Generate QR code when screen loads
    LaunchedEffect(Unit) {
        generateQRCode(context, onGenerateQR) { bitmap: android.graphics.Bitmap, stats: com.hanouti.app.data.DatabaseStats ->
            qrBitmap = bitmap
            databaseStats = stats
            lastGeneratedTime = System.currentTimeMillis()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.generate_sync_qr_code_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = com.hanouti.app.R.string.back))
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        isGenerating = true
                        generateQRCode(context, onGenerateQR) { bitmap: android.graphics.Bitmap, stats: com.hanouti.app.data.DatabaseStats ->
                            qrBitmap = bitmap
                            databaseStats = stats
                            lastGeneratedTime = System.currentTimeMillis()
                            isGenerating = false
                        }
                    },
                    enabled = !isGenerating
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(id = com.hanouti.app.R.string.regenerate))
                }
            }
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Database Statistics Card
            databaseStats?.let { stats ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            stringResource(id = com.hanouti.app.R.string.database_summary),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("Clients", stats.totalClients.toString())
                            StatItem("Products", stats.totalProducts.toString())
                            StatItem("Sales", stats.totalSales.toString())
                            StatItem("Bails", stats.totalSupplierBails.toString())
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            stringResource(id = com.hanouti.app.R.string.last_updated, formatTimestamp(stats.lastExportTime)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // QR Code Display
            if (isGenerating) {
                Card(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = com.hanouti.app.R.string.generating_qr_code),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                qrBitmap?.let { bitmap ->
                    // QR Code Card
                    Card(
                        modifier = Modifier
                            .size(300.dp)
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = stringResource(id = com.hanouti.app.R.string.sync_qr_code),
                                modifier = Modifier
                                    .size(280.dp)
                                    .padding(8.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // QR Code Info
                    Text(
                        stringResource(id = com.hanouti.app.R.string.sync_qr_code_generated),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    lastGeneratedTime?.let { time ->
                        Text(
                            stringResource(id = com.hanouti.app.R.string.generated_at, formatTimestamp(time)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            // Instructions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(id = com.hanouti.app.R.string.how_to_use_qr),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InstructionStep(
                        number = 1,
                        text = stringResource(id = com.hanouti.app.R.string.step_show_qr)
                    )
                    
                    InstructionStep(
                        number = 2,
                        text = stringResource(id = com.hanouti.app.R.string.step_scan_option)
                    )
                    
                    InstructionStep(
                        number = 3,
                        text = stringResource(id = com.hanouti.app.R.string.step_sync_auto)
                    )
                    
                    InstructionStep(
                        number = 4,
                        text = stringResource(id = com.hanouti.app.R.string.step_both_up_to_date)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        stringResource(id = com.hanouti.app.R.string.tip_generate_new_qr),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun InstructionStep(number: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                number.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun generateQRCode(
    context: Context,
    onGenerateQR: () -> String,
    onComplete: (android.graphics.Bitmap, com.hanouti.app.data.DatabaseStats) -> Unit
) {
    try {
        val exporter = com.hanouti.app.data.DatabaseExporter(context)
        val qrData = exporter.generateQRData()
        val qrBitmap = generateQRCodeBitmap(qrData, 800, 800)
        val stats = exporter.getDatabaseStats()
        onComplete(qrBitmap, stats)
    } catch (e: Exception) {
        // Handle error
        e.printStackTrace()
    }
}

private fun generateQRCodeBitmap(content: String, width: Int, height: Int): android.graphics.Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height)
    
    val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.RGB_565)
    
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    
    return bitmap
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
