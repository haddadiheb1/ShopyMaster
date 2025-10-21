package com.hanouti.app.ui.screens

import android.Manifest
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.hanouti.app.viewmodel.SalesViewModel
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    salesViewModel: SalesViewModel,
    onBack: () -> Unit,
    onScanned: () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()) { granted ->
        permissionGranted = granted
    }
    LaunchedEffect(Unit) { launcher.launch(Manifest.permission.CAMERA) }

    var pendingBarcode by remember { mutableStateOf<String?>(null) }
    var completed by remember { mutableStateOf(false) }
    var showNewProductDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newPriceText by remember { mutableStateOf("") }

    if (!permissionGranted) {
        Box(Modifier.fillMaxSize()) {
            Text("Camera permission is required", modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
        return
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val analyzer = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val scanner = BarcodeScanning.getClient()
                val isHandling = java.util.concurrent.atomic.AtomicBoolean(false)
                analyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null && !isHandling.get() && !completed) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        isHandling.set(true)
                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                val code = barcodes.firstOrNull()?.rawValue
                                if (code != null && !completed) {
                                    pendingBarcode = code
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                                isHandling.set(false)
                            }
                    } else {
                        imageProxy.close()
                    }
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }, modifier = Modifier.fillMaxSize())
    }

    val barcode = pendingBarcode
    LaunchedEffect(barcode) {
        val code = barcode ?: return@LaunchedEffect
        // Reset to avoid multiple triggers for the same frame
        pendingBarcode = null
        salesViewModel.getProduct(code) { product ->
            if (product != null) {
                salesViewModel.addProduct(product)
                completed = true
                onScanned()
            } else {
                showNewProductDialog = true
            }
        }
    }

    if (showNewProductDialog) {
        AlertDialog(
            onDismissRequest = { showNewProductDialog = false },
            title = { Text(stringResource(id = com.hanouti.app.R.string.new_product)) },
            text = {
                Column {
                    Text(stringResource(id = com.hanouti.app.R.string.barcode_x, barcode ?: ""))
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.product_name)) })
                    OutlinedTextField(value = newPriceText, onValueChange = { newPriceText = it }, label = { Text(stringResource(id = com.hanouti.app.R.string.price)) })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val price = newPriceText.toDoubleOrNull() ?: 0.0
                    val code = barcode ?: return@Button
                    if (newName.isNotBlank() && price > 0.0) {
                        salesViewModel.upsertProduct(code, newName.trim(), price) { saved ->
                            salesViewModel.addProduct(saved)
                            completed = true
                            onScanned()
                        }
                        showNewProductDialog = false
                    }
                }) { Text(stringResource(id = com.hanouti.app.R.string.save)) }
            },
            dismissButton = { OutlinedButton(onClick = { showNewProductDialog = false }) { Text(stringResource(id = com.hanouti.app.R.string.cancel)) } }
        )
    }
}


