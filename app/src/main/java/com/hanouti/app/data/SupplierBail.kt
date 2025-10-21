package com.hanouti.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier_bails")
data class SupplierBail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val supplierName: String,
    val amount: Double,
    val photoUri: String?,
    val timestampMillis: Long = System.currentTimeMillis()
)


