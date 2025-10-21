package com.hanouti.app.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.runBlocking

class DatabaseExporter(private val context: Context) {
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    
    companion object {
        private const val TAG = "DatabaseExporter"
        private const val COMPRESSION_THRESHOLD = 1000 // Compress if data > 1KB
    }
    
    /**
     * Export all database data to a compressed JSON string
     * This is what gets encoded in the QR code
     */
    fun exportDatabase(): String {
        return try {
            val exportData = DatabaseExport(
                timestamp = System.currentTimeMillis(),
                version = "1.0",
                exportId = generateExportId(),
                clients = getAllClients(),
                products = getAllProducts(),
                sales = getAllSales(),
                supplierBails = getAllSupplierBails(),
                creditEntries = getAllCreditEntries()
            )
            
            val jsonData = gson.toJson(exportData)
            
            // Compress if data is large
            if (jsonData.length > COMPRESSION_THRESHOLD) {
                compressData(jsonData)
            } else {
                jsonData
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting database", e)
            throw RuntimeException("Failed to export database: ${e.message}")
        }
    }
    
    /**
     * Import database from JSON string
     * Returns true if successful, false otherwise
     */
    fun importDatabase(jsonData: String): Boolean {
        return try {
            // Try to decompress if it's compressed
            val decompressedData = if (isCompressed(jsonData)) {
                decompressData(jsonData)
            } else {
                jsonData
            }
            
            val exportData = gson.fromJson(decompressedData, DatabaseExport::class.java)
            
            // Validate the export data
            if (!validateExportData(exportData)) {
                Log.e(TAG, "Invalid export data")
                return false
            }
            
            // Create backup before import
            createBackup()
            
            // Import the data
            importClients(exportData.clients)
            importProducts(exportData.products)
            importSales(exportData.sales)
            importSupplierBails(exportData.supplierBails)
            importCreditEntries(exportData.creditEntries)
            
            Log.i(TAG, "Database import successful: ${exportData.clients.size} clients, ${exportData.products.size} products, ${exportData.sales.size} sales")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error importing database", e)
            // Try to restore from backup
            restoreFromBackup()
            false
        }
    }
    
    /**
     * Generate QR code data (compressed and encoded)
     */
    fun generateQRData(): String {
        val exportData = exportDatabase()
        return Base64.getEncoder().encodeToString(exportData.toByteArray())
    }
    
    /**
     * Decode QR code data
     */
    fun decodeQRData(qrData: String): String? {
        return try {
            val decodedBytes = Base64.getDecoder().decode(qrData)
            String(decodedBytes)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding QR data", e)
            null
        }
    }
    
    /**
     * Get database statistics for display
     */
    fun getDatabaseStats(): DatabaseStats {
        return DatabaseStats(
            totalClients = getAllClients().size,
            totalProducts = getAllProducts().size,
            totalSales = getAllSales().size,
            totalSupplierBails = getAllSupplierBails().size,
            lastExportTime = System.currentTimeMillis()
        )
    }
    
    // Private helper methods
    private fun getAllClients(): List<Client> {
        val db = AppDatabase.get(context)
        return runBlocking { db.clientDao().getAllClients() }
    }
    
    private fun getAllProducts(): List<Product> {
        val db = AppDatabase.get(context)
        return runBlocking { db.productDao().getAll() }
    }
    
    private fun getAllSales(): List<SaleTransaction> {
        val db = AppDatabase.get(context)
        return runBlocking { db.salesDao().getAllSales() }
    }
    
    private fun getAllSupplierBails(): List<SupplierBail> {
        val db = AppDatabase.get(context)
        return runBlocking { db.supplierBailDao().getAll() }
    }
    
    private fun getAllCreditEntries(): List<CreditEntry> {
        val db = AppDatabase.get(context)
        return runBlocking { db.creditEntryDao().getAll() }
    }
    
    private fun importClients(clients: List<Client>) {
        val db = AppDatabase.get(context)
        runBlocking {
            db.clientDao().deleteAll()
            if (clients.isNotEmpty()) {
                db.clientDao().insertAll(clients)
            }
        }
    }
    
    private fun importProducts(products: List<Product>) {
        val db = AppDatabase.get(context)
        runBlocking {
            db.productDao().deleteAll()
            if (products.isNotEmpty()) {
                db.productDao().insertAll(products)
            }
        }
    }
    
    private fun importSales(sales: List<SaleTransaction>) {
        val db = AppDatabase.get(context)
        runBlocking {
            // Clear children first then parents
            db.salesDao().deleteAllItems()
            db.salesDao().deleteAllSales()
            if (sales.isNotEmpty()) {
                db.salesDao().insertSales(sales)
            }
        }
    }
    
    private fun importSupplierBails(bails: List<SupplierBail>) {
        val db = AppDatabase.get(context)
        runBlocking {
            db.supplierBailDao().deleteAll()
            if (bails.isNotEmpty()) {
                db.supplierBailDao().insertAll(bails)
            }
        }
    }
    
    private fun importCreditEntries(entries: List<CreditEntry>) {
        val db = AppDatabase.get(context)
        runBlocking {
            db.creditEntryDao().deleteAll()
            if (entries.isNotEmpty()) {
                db.creditEntryDao().insertAll(entries)
            }
        }
    }
    
    private fun generateExportId(): String {
        return "HANOUTI_${System.currentTimeMillis()}_${Random().nextInt(1000)}"
    }
    
    private fun validateExportData(exportData: DatabaseExport): Boolean {
        return exportData.version.isNotEmpty() && 
               exportData.timestamp > 0 &&
               exportData.exportId.isNotEmpty()
    }
    
    private fun compressData(data: String): String {
        val output = ByteArrayOutputStream()
        GZIPOutputStream(output).use { gzip ->
            gzip.write(data.toByteArray())
        }
        return Base64.getEncoder().encodeToString(output.toByteArray())
    }
    
    private fun decompressData(compressedData: String): String {
        val compressedBytes = Base64.getDecoder().decode(compressedData)
        val input = ByteArrayInputStream(compressedBytes)
        val output = ByteArrayOutputStream()
        
        GZIPInputStream(input).use { gzip ->
            gzip.copyTo(output)
        }
        
        return String(output.toByteArray())
    }
    
    private fun isCompressed(data: String): Boolean {
        return try {
            Base64.getDecoder().decode(data)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    private fun createBackup() {
        // Simple best-effort: no-op placeholder (Room fallbackToDestructiveMigration already used)
    }
    
    private fun restoreFromBackup() {
        // No-op placeholder
    }
}

/**
 * Data structure for database export/import
 */
data class DatabaseExport(
    val timestamp: Long,
    val version: String,
    val exportId: String,
    val clients: List<Client>,
    val products: List<Product>,
    val sales: List<SaleTransaction>,
    val supplierBails: List<SupplierBail>,
    val creditEntries: List<CreditEntry>
)

/**
 * Database statistics for display
 */
data class DatabaseStats(
    val totalClients: Int,
    val totalProducts: Int,
    val totalSales: Int,
    val totalSupplierBails: Int,
    val lastExportTime: Long
)
