package com.hanouti.app.data.dao

import androidx.room.*
import com.hanouti.app.data.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun observeClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients ORDER BY name ASC")
    suspend fun getAllClients(): List<Client>

    @Query("SELECT * FROM clients WHERE id = :id")
    fun observeClientById(id: Int): Flow<Client?>

    @Insert
    suspend fun insert(client: Client): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clients: List<Client>)

    @Update
    suspend fun update(client: Client)

    @Query("UPDATE clients SET credit = :credit WHERE id = :id")
    suspend fun updateCredit(id: Int, credit: Double)

    @Query("DELETE FROM clients WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM clients")
    suspend fun deleteAll()
}


