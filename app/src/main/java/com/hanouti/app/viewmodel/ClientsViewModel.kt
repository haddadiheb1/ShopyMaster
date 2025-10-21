package com.hanouti.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanouti.app.data.Client
import com.hanouti.app.data.CreditEntry
import com.hanouti.app.data.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ClientsViewModel(
    private val repository: Repository
) : ViewModel() {
    fun observeClients(): Flow<List<Client>> = repository.observeClients()
    var searchQuery: String by mutableStateOf("")

    private var _clients: List<Client> by mutableStateOf(emptyList())
    val clients: List<Client> get() = _clients

    init {
        viewModelScope.launch {
            repository.observeClients().collect { list ->
                _clients = list
            }
        }
    }

    fun observeClient(id: Int): Flow<Client?> = repository.observeClient(id)
    fun observeCreditHistory(clientId: Int): Flow<List<CreditEntry>> = repository.observeCreditHistory(clientId)

    fun addClient(name: String, credit: Double) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repository.addClient(trimmed, credit) }
    }

    fun applyCreditChange(clientId: Int, deltaAmount: Double, note: String? = null) {
        viewModelScope.launch { repository.applyCreditChange(clientId, deltaAmount, note) }
    }

    fun deleteClient(clientId: Int) {
        viewModelScope.launch { repository.deleteClient(clientId) }
    }

    fun deleteCreditEntry(entry: CreditEntry) {
        viewModelScope.launch { repository.deleteCreditEntry(entry) }
    }

    fun updateCreditEntry(entry: CreditEntry, newDeltaAmount: Double, newNote: String?) {
        viewModelScope.launch { repository.updateCreditEntry(entry, newDeltaAmount, newNote) }
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ClientsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
