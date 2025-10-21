package com.hanouti.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanouti.app.data.Product
import com.hanouti.app.data.Repository
import kotlinx.coroutines.launch

class SalesViewModel(private val repository: Repository) : ViewModel() {
    data class ScannedLine(val product: Product, var quantity: Int = 1)

    private var _lines by mutableStateOf<List<ScannedLine>>(emptyList())
    val lines: List<ScannedLine> get() = _lines

    val total: Double get() = _lines.sumOf { it.product.price * it.quantity }

    fun addProduct(product: Product) {
        val existing = _lines.indexOfFirst { it.product.barcode == product.barcode }
        _lines = if (existing >= 0) {
            _lines.toMutableList().also { it[existing] = it[existing].copy(quantity = it[existing].quantity + 1) }
        } else {
            _lines + ScannedLine(product, 1)
        }
    }

    fun decrementProduct(barcode: String) {
        val idx = _lines.indexOfFirst { it.product.barcode == barcode }
        if (idx >= 0) {
            val list = _lines.toMutableList()
            val line = list[idx]
            if (line.quantity > 1) list[idx] = line.copy(quantity = line.quantity - 1) else list.removeAt(idx)
            _lines = list
        }
    }

    fun removeProduct(barcode: String): ScannedLine? {
        val idx = _lines.indexOfFirst { it.product.barcode == barcode }
        if (idx >= 0) {
            val list = _lines.toMutableList()
            val removed = list.removeAt(idx)
            _lines = list
            return removed
        }
        return null
    }

    fun addLine(line: ScannedLine) {
        val existing = _lines.indexOfFirst { it.product.barcode == line.product.barcode }
        _lines = if (existing >= 0) {
            _lines.toMutableList().also { it[existing] = it[existing].copy(quantity = it[existing].quantity + line.quantity) }
        } else {
            _lines + line
        }
    }

    fun clear() { _lines = emptyList() }

    fun upsertProduct(barcode: String, name: String, price: Double, onDone: (Product) -> Unit) {
        viewModelScope.launch {
            repository.upsertProduct(barcode, name, price)
            val saved = repository.getProduct(barcode) ?: Product(barcode, name, price)
            onDone(saved)
        }
    }

    fun getProduct(barcode: String, onResult: (Product?) -> Unit) {
        viewModelScope.launch { onResult(repository.getProduct(barcode)) }
    }

    fun finishTransaction(onSaved: (Int) -> Unit) {
        viewModelScope.launch {
            val items = _lines.map { it.product to it.quantity }
            val id = repository.saveSale(items)
            clear()
            onSaved(id)
        }
    }

    fun addManualProduct(name: String, price: Double, saveToCatalog: Boolean, onDone: (() -> Unit)? = null) {
        val barcode = "MANUAL-" + java.util.UUID.randomUUID().toString()
        val product = Product(barcode = barcode, name = name.trim(), price = price)
        addProduct(product)
        if (saveToCatalog) {
            viewModelScope.launch {
                repository.upsertProduct(product.barcode, product.name, product.price)
                onDone?.let { it() }
            }
        } else {
            onDone?.let { it() }
        }
    }

    fun addManualAmount(price: Double) {
        val barcode = "MANUAL-" + java.util.UUID.randomUUID().toString()
        val product = Product(barcode = barcode, name = "Item", price = price)
        addProduct(product)
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SalesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


