package com.hanouti.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hanouti.app.data.CreditEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditEntryDao {
    @Query("SELECT * FROM credit_entries WHERE clientId = :clientId ORDER BY timestampMillis DESC")
    fun observeForClient(clientId: Int): Flow<List<CreditEntry>>

    @Query("SELECT * FROM credit_entries ORDER BY timestampMillis DESC")
    suspend fun getAll(): List<CreditEntry>

    @Insert
    suspend fun insert(entry: CreditEntry): Long

    @Insert
    suspend fun insertAll(entries: List<CreditEntry>)

    @Delete
    suspend fun delete(entry: CreditEntry)

    @Query("DELETE FROM credit_entries WHERE clientId = :clientId")
    suspend fun deleteForClient(clientId: Int)

    @Update
    suspend fun update(entry: CreditEntry)

    @Query("DELETE FROM credit_entries")
    suspend fun deleteAll()
}


