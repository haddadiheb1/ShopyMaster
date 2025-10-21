package com.hanouti.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val credit: Double
)
