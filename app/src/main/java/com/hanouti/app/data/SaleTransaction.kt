package com.hanouti.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestampMillis: Long = System.currentTimeMillis(),
    val totalAmount: Double
)

@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = SaleTransaction::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("transactionId"), Index("productBarcode")]
)
data class SaleItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transactionId: Int,
    val productBarcode: String,
    val productName: String,
    val priceAtSale: Double,
    val quantity: Int = 1
)


