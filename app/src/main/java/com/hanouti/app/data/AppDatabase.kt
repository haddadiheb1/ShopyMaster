package com.hanouti.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hanouti.app.data.dao.ClientDao
import com.hanouti.app.data.dao.CreditEntryDao
import com.hanouti.app.data.dao.ProductDao
import com.hanouti.app.data.dao.SalesDao

@Database(
    entities = [Client::class, CreditEntry::class, SupplierBail::class, Product::class, SaleTransaction::class, SaleItem::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun creditEntryDao(): CreditEntryDao
    abstract fun supplierBailDao(): com.hanouti.app.data.dao.SupplierBailDao
    abstract fun productDao(): ProductDao
    abstract fun salesDao(): SalesDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "hanouti.db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}


