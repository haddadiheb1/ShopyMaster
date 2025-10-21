package com.hanouti.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanouti.app.data.Repository
import com.hanouti.app.data.SaleItem
import com.hanouti.app.data.SaleTransaction
import com.hanouti.app.data.dao.DailyTotalRow
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(private val repository: Repository) : ViewModel() {
    fun observeDailyTotals(): Flow<List<DailyTotalRow>> = repository.observeDailyTotals()
    fun observeSalesForDate(date: String): Flow<List<SaleTransaction>> = repository.observeSalesForDate(date)
    fun observeItemsForSale(transactionId: Int): Flow<List<SaleItem>> = repository.observeItemsForSale(transactionId)

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


