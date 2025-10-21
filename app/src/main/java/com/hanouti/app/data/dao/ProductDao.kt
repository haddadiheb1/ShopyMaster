package com.hanouti.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hanouti.app.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getByBarcode(barcode: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAll(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAll(): List<Product>

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("DELETE FROM products WHERE barcode = :barcode")
    suspend fun deleteByBarcode(barcode: String)
}


