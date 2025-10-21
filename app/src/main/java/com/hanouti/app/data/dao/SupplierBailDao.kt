package com.hanouti.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hanouti.app.data.SupplierBail
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierBailDao {
    @Query("SELECT * FROM supplier_bails ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<SupplierBail>>

    @Query("SELECT * FROM supplier_bails ORDER BY timestampMillis DESC")
    suspend fun getAll(): List<SupplierBail>

    @Query("SELECT * FROM supplier_bails WHERE id = :id")
    fun observeById(id: Int): Flow<SupplierBail?>

    @Insert
    suspend fun insert(bail: SupplierBail): Long

    @Insert
    suspend fun insertAll(bails: List<SupplierBail>)

    @Delete
    suspend fun delete(bail: SupplierBail)

    @androidx.room.Update
    suspend fun update(bail: SupplierBail)

    @Query("DELETE FROM supplier_bails")
    suspend fun deleteAll()
}


