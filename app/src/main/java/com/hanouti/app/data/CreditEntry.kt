package com.hanouti.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "credit_entries",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["clientId"]), Index(value = ["timestampMillis"])]
)
data class CreditEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int,
    val deltaAmount: Double,
    val timestampMillis: Long = System.currentTimeMillis(),
    val note: String? = null
)


