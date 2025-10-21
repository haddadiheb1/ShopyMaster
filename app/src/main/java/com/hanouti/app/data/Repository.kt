package com.hanouti.app.data

import com.hanouti.app.data.dao.ClientDao
import com.hanouti.app.data.dao.CreditEntryDao
import com.hanouti.app.data.dao.SupplierBailDao
import com.hanouti.app.data.dao.ProductDao
import com.hanouti.app.data.dao.SalesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class Repository(
    private val clientDao: ClientDao,
    private val creditEntryDao: CreditEntryDao,
    private val supplierBailDao: SupplierBailDao,
    private val productDao: ProductDao,
    private val salesDao: SalesDao
) {
    fun observeClients(): Flow<List<Client>> = clientDao.observeClients()
    fun observeClient(id: Int): Flow<Client?> = clientDao.observeClientById(id)
    fun observeCreditHistory(clientId: Int): Flow<List<CreditEntry>> = creditEntryDao.observeForClient(clientId)

    suspend fun addClient(name: String, initialCredit: Double) {
        val id = clientDao.insert(Client(name = name.trim(), credit = initialCredit)).toInt()
        creditEntryDao.insert(CreditEntry(clientId = id, deltaAmount = initialCredit, note = "Initial balance"))
    }

    suspend fun applyCreditChange(clientId: Int, deltaAmount: Double, note: String?) {
        val client = observeClient(clientId).first() ?: return
        clientDao.updateCredit(clientId, client.credit + deltaAmount)
        creditEntryDao.insert(CreditEntry(clientId = clientId, deltaAmount = deltaAmount, note = note))
    }

    suspend fun deleteClient(clientId: Int) {
        clientDao.deleteById(clientId)
    }

    suspend fun deleteCreditEntry(entry: CreditEntry) {
        // Reverse its effect on the client's balance then remove the entry
        val client = observeClient(entry.clientId).first() ?: return
        clientDao.updateCredit(entry.clientId, client.credit - entry.deltaAmount)
        creditEntryDao.delete(entry)
    }

    suspend fun updateCreditEntry(original: CreditEntry, newDeltaAmount: Double, newNote: String?) {
        val client = observeClient(original.clientId).first() ?: return
        val deltaDiff = newDeltaAmount - original.deltaAmount
        // Apply the difference to client's credit
        clientDao.updateCredit(original.clientId, client.credit + deltaDiff)
        // Update entry (keep same id and timestamp, modify amount and note)
        val updated = original.copy(deltaAmount = newDeltaAmount, note = newNote)
        creditEntryDao.update(updated)
    }

    // Supplier bails
    fun observeSupplierBails(): Flow<List<SupplierBail>> = supplierBailDao.observeAll()
    fun observeSupplierBail(id: Int): Flow<SupplierBail?> = supplierBailDao.observeById(id)
    suspend fun addSupplierBail(name: String, amount: Double, photoUri: String?) {
        supplierBailDao.insert(SupplierBail(supplierName = name.trim(), amount = amount, photoUri = photoUri))
    }
    suspend fun deleteSupplierBail(bail: SupplierBail) = supplierBailDao.delete(bail)
    suspend fun updateSupplierBail(bail: SupplierBail) = supplierBailDao.update(bail)

    // Products
    suspend fun upsertProduct(barcode: String, name: String, price: Double) {
        productDao.upsert(Product(barcode = barcode, name = name.trim(), price = price))
    }
    suspend fun getProduct(barcode: String): Product? = productDao.getByBarcode(barcode)
    fun observeProducts(): Flow<List<Product>> = productDao.observeAll()
    suspend fun updateProduct(product: Product) = productDao.update(product)
    suspend fun deleteProduct(barcode: String) = productDao.deleteByBarcode(barcode)

    // Sales
    fun observeDailyTotals() = salesDao.observeDailyTotals()
    fun observeSalesForDate(date: String) = salesDao.observeSalesForDate(date)
    fun observeItemsForSale(transactionId: Int) = salesDao.observeItemsForSale(transactionId)
    fun observeSalesCount() = salesDao.observeSalesCount()
    fun observeTodaySalesAmount() = salesDao.observeTodaySalesAmount()
    fun observeSalesCountInRange(startMillis: Long, endMillis: Long) = salesDao.observeSalesCountInRange(startMillis, endMillis)
    fun observeSalesAmountInRange(startMillis: Long, endMillis: Long) = salesDao.observeSalesAmountInRange(startMillis, endMillis)
    suspend fun saveSale(items: List<Pair<Product, Int>>): Int {
        val total = items.sumOf { (p, q) -> p.price * q }
        val saleId = salesDao.insertSale(SaleTransaction(totalAmount = total)).toInt()
        val saleItems = items.flatMap { (p, q) ->
            List(q) { SaleItem(transactionId = saleId, productBarcode = p.barcode, productName = p.name, priceAtSale = p.price, quantity = 1) }
        }
        salesDao.insertItems(saleItems)
        return saleId
    }
}


