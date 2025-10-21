package com.hanouti.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanouti.app.data.SaleItem
import com.hanouti.app.data.SaleTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSale(sale: SaleTransaction): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItems(items: List<SaleItem>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSales(sales: List<SaleTransaction>): List<Long>

    @Query("SELECT date(timestampMillis/1000, 'unixepoch', 'localtime') AS day, SUM(totalAmount) AS total FROM sales GROUP BY day ORDER BY day DESC")
    fun observeDailyTotals(): Flow<List<DailyTotalRow>>

    @Query("SELECT * FROM sales WHERE date(timestampMillis/1000, 'unixepoch', 'localtime') = :date ORDER BY timestampMillis DESC")
    fun observeSalesForDate(date: String): Flow<List<SaleTransaction>>

    @Query("SELECT * FROM sale_items WHERE transactionId = :transactionId")
    fun observeItemsForSale(transactionId: Int): Flow<List<SaleItem>>

    @Query("SELECT COUNT(*) FROM sales")
    fun observeSalesCount(): Flow<Int>

    @Query("SELECT IFNULL(SUM(totalAmount), 0) FROM sales WHERE date(timestampMillis/1000, 'unixepoch', 'localtime') = date('now','localtime')")
    fun observeTodaySalesAmount(): Flow<Double>

    @Query("SELECT COUNT(*) FROM sales WHERE timestampMillis BETWEEN :startMillis AND :endMillis")
    fun observeSalesCountInRange(startMillis: Long, endMillis: Long): Flow<Int>

    @Query("SELECT IFNULL(SUM(totalAmount), 0) FROM sales WHERE timestampMillis BETWEEN :startMillis AND :endMillis")
    fun observeSalesAmountInRange(startMillis: Long, endMillis: Long): Flow<Double>

    @Query("SELECT * FROM sales ORDER BY timestampMillis DESC")
    suspend fun getAllSales(): List<SaleTransaction>

    @Query("DELETE FROM sale_items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM sales")
    suspend fun deleteAllSales()
}

data class DailyTotalRow(
    val day: String,
    val total: Double
)


