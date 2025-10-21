package com.hanouti.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import androidx.activity.compose.rememberLauncherForActivityResult
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    onBack: () -> Unit,
    onQRScanned: (String) -> Boolean
) {
    var isScanning by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf<String?>(null) }
    var isImporting by remember { mutableStateOf(false) }
    var importSuccess by remember { mutableStateOf<Boolean?>(null) }
    var importProgress by remember { mutableStateOf(0f) }
    var importDetails by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val scanPrompt = stringResource(id = com.hanouti.app.R.string.scan_sync_qr)
    
    var scannedData by remember { mutableStateOf<String?>(null) }
    
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents != null) {
            scannedData = result.contents
        }
    }
    
    // Handle import when scanned data changes
    LaunchedEffect(scannedData) {
        scannedData?.let { qrData ->
            scanResult = qrData
            isImporting = true
            importProgress = 0f
            
            // Simulate import progress
            for (i in 1..100) {
                importProgress = i / 100f
                kotlinx.coroutines.delay(50) // 50ms per step = 5 seconds total
            }
            
            // Try to import the scanned data
            val success = onQRScanned(qrData)
            importSuccess = success
            isImporting = false
            
            if (success) {
                importDetails = "Data imported successfully at ${formatTimestamp(System.currentTimeMillis())}"
            } else {
                importDetails = "Failed to import data. The QR code may be corrupted or invalid."
            }
            
            // Clear scanned data
            scannedData = null
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.scan_sync_qr)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = com.hanouti.app.R.string.back))
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
            when {
                isImporting -> {
                    // Import Progress State
                    ImportProgressState(
                        progress = importProgress,
                        onCancel = {
                            isImporting = false
                            importSuccess = null
                            scanResult = null
                        }
                    )
                }
                
                importSuccess == true -> {
                    // Success State
                    SuccessState(
                        details = importDetails,
                        onContinue = onBack
                    )
                }
                
                importSuccess == false -> {
                    // Error State
                    ErrorState(
                        details = importDetails,
                        onTryAgain = {
                            importSuccess = null
                            scanResult = null
                        }
                    )
                }
                
                else -> {
                    // Initial State
                    InitialState(
                        onStartScanning = {
                            isScanning = true
                            scanLauncher.launch(
                                ScanOptions()
                                    .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                                     .setPrompt(scanPrompt)
                                    .setBeepEnabled(true)
                                    .setBarcodeImageEnabled(true)
                                    .setTorchEnabled(false)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InitialState(onStartScanning: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Scanner Icon
        Card(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QrCodeScanner,
                    contentDescription = stringResource(id = com.hanouti.app.R.string.scan_sync_qr),
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Ready to Sync Data",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "Point your camera at the sync QR code from the other worker",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Start Scanning Button
        Button(
            onClick = onStartScanning,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Start Scanning",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
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
                Text(
                    "📱 How to Sync:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                InstructionStep(
                    number = 1,
                    text = "Ask the current worker to generate a sync QR code"
                )
                
                InstructionStep(
                    number = 2,
                    text = "Point your camera at the displayed QR code"
                )
                
                InstructionStep(
                    number = 3,
                    text = "Wait for the import to complete"
                )
                
                InstructionStep(
                    number = 4,
                    text = "You'll have all the latest data!"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "⚠️ Important: This will replace all current data with the imported data. Make sure you want to proceed.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ImportProgressState(
    progress: Float,
    onCancel: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        // Progress Indicator
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(120.dp),
            strokeWidth = 8.dp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Importing Data...",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "${(progress * 100).toInt()}% Complete",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Please wait while we synchronize your data...",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Cancel Button
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = com.hanouti.app.R.string.cancel))
        }
    }
}

@Composable
private fun SuccessState(
    details: String?,
    onContinue: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        // Success Icon
        Card(
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = stringResource(id = com.hanouti.app.R.string.success),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "🎉 Sync Successful!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "All data has been synchronized",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        details?.let { detail ->
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    detail,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(stringResource(id = com.hanouti.app.R.string.continue_working))
        }
    }
}

@Composable
private fun ErrorState(
    details: String?,
    onTryAgain: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        // Error Icon
        Card(
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Error,
                    contentDescription = stringResource(id = com.hanouti.app.R.string.error),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "❌ Sync Failed",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "The QR code data is invalid or corrupted",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        details?.let { detail ->
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    detail,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onTryAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = com.hanouti.app.R.string.try_again))
        }
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
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                number.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondaryContainer
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

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
