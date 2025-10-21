package com.hanouti.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanouti.app.data.Product
import com.hanouti.app.data.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: Repository) : ViewModel() {
    fun observeProducts(): Flow<List<Product>> = repository.observeProducts()

     fun updateProduct(barcode: String, name: String, price: Double) {
         viewModelScope.launch {
             repository.updateProduct(Product(barcode = barcode, name = name.trim(), price = price))
         }
     }

     fun deleteProduct(barcode: String) {
         viewModelScope.launch { repository.deleteProduct(barcode) }
     }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


