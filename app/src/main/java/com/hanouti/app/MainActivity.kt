package com.hanouti.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanouti.app.ui.theme.HanoutiTheme
import com.hanouti.app.viewmodel.ClientsViewModel
import com.hanouti.app.navigation.AppNavHost
import com.hanouti.app.data.AppDatabase
import com.hanouti.app.data.Repository
import com.hanouti.app.settings.LanguagePreferences
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved locale synchronously before composing UI
        val lang = runBlocking { LanguagePreferences.getLanguage(applicationContext) }
        LanguagePreferences.applyLocale(lang)
        setContent {
            HanoutiTheme {
                val db = AppDatabase.get(applicationContext)
                val repository = Repository(db.clientDao(), db.creditEntryDao(), db.supplierBailDao(), db.productDao(), db.salesDao())
                val clientsViewModel: ClientsViewModel = viewModel(factory = ClientsViewModel.Factory(repository))
                val supplierBailsViewModel: com.hanouti.app.viewmodel.SupplierBailsViewModel = viewModel(factory = com.hanouti.app.viewmodel.SupplierBailsViewModel.Factory(repository))
                AppNavHost(clientsViewModel = clientsViewModel, supplierBailsViewModel = supplierBailsViewModel)
            }
        }
    }
}
