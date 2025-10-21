package com.hanouti.app.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import com.hanouti.app.data.Client
import com.hanouti.app.data.CreditEntry
import com.hanouti.app.data.SaleTransaction
import com.hanouti.app.data.SaleItem

object CsvExporter {
    fun exportClientsCredits(
        context: Context,
        clients: List<Client>,
        credits: Map<Int, List<CreditEntry>>,
        fileName: String = "Hanouti_ClientCredits.csv"
    ): File? {
        val sb = StringBuilder()
        sb.append("Client Name,Credit,History\n")
        for (client in clients) {
            val history = credits[client.id]?.joinToString("; ") { "${it.deltaAmount} (${it.note ?: ""})" } ?: ""
            sb.append("${client.name},${client.credit},${history}\n")
        }
        return writeCsvFile(context, fileName, sb.toString())
    }

    fun exportSalesHistory(
        context: Context,
        sales: List<SaleTransaction>,
        saleItems: Map<Int, List<SaleItem>>,
        fileName: String = "Hanouti_SalesHistory.csv"
    ): File? {
        val sb = StringBuilder()
        sb.append("Date,Total Amount,Items\n")
        for (sale in sales) {
            val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(sale.timestampMillis))
            val items = saleItems[sale.id]?.joinToString("; ") { "${it.productName}: ${it.quantity}" } ?: ""
            sb.append("$date,${sale.totalAmount},${items}\n")
        }
        return writeCsvFile(context, fileName, sb.toString())
    }

    private fun writeCsvFile(context: Context, fileName: String, content: String): File? {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (dir != null && (dir.exists() || dir.mkdirs())) {
            val file = File(dir, fileName)
            FileOutputStream(file).use { it.write(content.toByteArray()) }
            return file
        }
        return null
    }
}
