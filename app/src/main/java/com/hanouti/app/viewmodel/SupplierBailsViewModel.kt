package com.hanouti.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanouti.app.data.Repository
import com.hanouti.app.data.SupplierBail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SupplierBailsViewModel(private val repository: Repository) : ViewModel() {
    val bails: Flow<List<SupplierBail>> = repository.observeSupplierBails()
    fun observeBail(id: Int) = repository.observeSupplierBail(id)

    fun addBail(name: String, amount: Double, photoUri: String?) {
        viewModelScope.launch { repository.addSupplierBail(name, amount, photoUri) }
    }

    fun deleteBail(bail: SupplierBail) {
        viewModelScope.launch { repository.deleteSupplierBail(bail) }
    }

    fun updateBail(bail: SupplierBail) {
        viewModelScope.launch { repository.updateSupplierBail(bail) }
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SupplierBailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SupplierBailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


