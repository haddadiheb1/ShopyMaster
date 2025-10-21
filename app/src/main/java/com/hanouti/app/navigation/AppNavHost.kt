package com.hanouti.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hanouti.app.ui.screens.AddClientScreen
import com.hanouti.app.ui.screens.ClientDetailScreen
import com.hanouti.app.ui.screens.ClientsScreen
import com.hanouti.app.ui.screens.SupplierBailsScreen
import com.hanouti.app.ui.screens.SupplierBailDetailScreen
import com.hanouti.app.ui.screens.SplashScreen
import com.hanouti.app.ui.screens.SalesScreen
import com.hanouti.app.ui.screens.ScannerScreen
import com.hanouti.app.ui.screens.HistoryScreen
import com.hanouti.app.ui.screens.ProductsScreen
import com.hanouti.app.ui.screens.QRGeneratorScreen
import com.hanouti.app.ui.screens.QRScannerScreen
import com.hanouti.app.viewmodel.ClientsViewModel
import com.hanouti.app.viewmodel.SupplierBailsViewModel
import com.hanouti.app.viewmodel.SalesViewModel
import com.hanouti.app.viewmodel.HistoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.hanouti.app.viewmodel.ProductsViewModel
import kotlinx.coroutines.flow.first
import com.hanouti.app.ui.screens.SettingsScreen

object Routes {
    const val Splash = "splash"
    const val Dashboard = "dashboard"
    const val Clients = "clients"
    const val AddClient = "addClient"
    const val ClientDetail = "clientDetail/{clientId}"
    const val SupplierBails = "supplierBails"
    const val SupplierBailDetail = "supplierBailDetail/{bailId}"
    const val Sales = "sales"
    const val Scanner = "scanner"
    const val History = "history"
    const val Products = "products"
    const val QRGenerator = "qrGenerator"
    const val QRScanner = "qrScanner"
    const val Settings = "settings"
    fun supplierBailDetail(id: Int) = "supplierBailDetail/$id"
    fun clientDetail(clientId: Int) = "clientDetail/$clientId"
}

@Composable
fun AppNavHost(
    clientsViewModel: ClientsViewModel,
    supplierBailsViewModel: SupplierBailsViewModel,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.dashboard)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Dashboard) {
                            popUpTo(Routes.Dashboard) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors()
                )
                Text(
                    text = stringResource(id = com.hanouti.app.R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.clients)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Clients) {
                            popUpTo(Routes.Clients) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors()
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.bails)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.SupplierBails) {
                            popUpTo(Routes.Clients) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.sales)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Sales) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.history)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.History) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.History, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.products)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Products) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.settings)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Settings) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.scan_product)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.Scanner) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.generate_sync_qr)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.QRGenerator) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.QrCode, contentDescription = null) }
                )
                
                NavigationDrawerItem(
                    label = { Text(stringResource(id = com.hanouti.app.R.string.scan_sync_qr)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.QRScanner) { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.Splash) {
            composable(Routes.Dashboard) {
                val context = LocalContext.current
                val productsViewModel: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory(
                    com.hanouti.app.data.Repository(
                        com.hanouti.app.data.AppDatabase.get(context).clientDao(),
                        com.hanouti.app.data.AppDatabase.get(context).creditEntryDao(),
                        com.hanouti.app.data.AppDatabase.get(context).supplierBailDao(),
                        com.hanouti.app.data.AppDatabase.get(context).productDao(),
                        com.hanouti.app.data.AppDatabase.get(context).salesDao()
                    )
                ))
                val clients by clientsViewModel.observeClients().collectAsState(initial = emptyList())
                val bails by supplierBailsViewModel.bails.collectAsState(initial = emptyList())
                val products by productsViewModel.observeProducts().collectAsState(initial = emptyList())
                var selectedStartMillis by remember { mutableStateOf<Long?>(null) }
                var selectedEndMillis by remember { mutableStateOf<Long?>(null) }

                fun normalizeStartOfDay(millis: Long): Long {
                    val cal = java.util.Calendar.getInstance()
                    cal.timeInMillis = millis
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                    return cal.timeInMillis
                }

                fun normalizeEndOfDay(millis: Long): Long {
                    val cal = java.util.Calendar.getInstance()
                    cal.timeInMillis = millis
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
                    cal.set(java.util.Calendar.MINUTE, 59)
                    cal.set(java.util.Calendar.SECOND, 59)
                    cal.set(java.util.Calendar.MILLISECOND, 999)
                    return cal.timeInMillis
                }
                val totalClients = clients.size
                val totalProducts = products.size
                val salesDao = com.hanouti.app.data.AppDatabase.get(context).salesDao()
                val totalSales by (
                    if (selectedStartMillis != null && selectedEndMillis != null) {
                        val s = normalizeStartOfDay(selectedStartMillis!!)
                        val e = normalizeEndOfDay(selectedEndMillis!!)
                        salesDao.observeSalesCountInRange(s, e)
                    } else {
                        salesDao.observeSalesCount()
                    }
                ).collectAsState(initial = 0)
                val totalCredit = clients.sumOf { it.credit }
                val salesAmount by (
                    if (selectedStartMillis != null && selectedEndMillis != null) {
                        val s = normalizeStartOfDay(selectedStartMillis!!)
                        val e = normalizeEndOfDay(selectedEndMillis!!)
                        salesDao.observeSalesAmountInRange(s, e)
                    } else {
                        salesDao.observeTodaySalesAmount()
                    }
                ).collectAsState(initial = 0.0)
                val totalBailsAmount = bails.sumOf { it.amount }
                val topClients = clients.sortedByDescending { it.credit }.take(3)
                val topProducts = products.take(3)
                com.hanouti.app.ui.screens.DashboardScreen(
                    totalClients = totalClients,
                    totalProducts = totalProducts,
                    totalSales = totalSales,
                    totalCredit = totalCredit,
                    todaySales = salesAmount,
                    totalBailsAmount = totalBailsAmount,
                    topClients = topClients,
                    topProducts = topProducts,
                    onBack = { navController.popBackStack() },
                    onDateRangeChange = { start, end ->
                        selectedStartMillis = start
                        selectedEndMillis = end
                    }
                )
            }
            
            composable(Routes.Splash) {
                SplashScreen(onFinished = {
                    navController.navigate(Routes.Clients) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                })
            }
            
            composable(Routes.Clients) {
                val getCreditHistory: suspend (Int) -> List<com.hanouti.app.data.CreditEntry> = { clientId -> 
                    clientsViewModel.observeCreditHistory(clientId).first() 
                }
                ClientsScreen(
                    clients = clientsViewModel.clients,
                    searchQuery = clientsViewModel.searchQuery,
                    onSearchQueryChange = { clientsViewModel.searchQuery = it },
                    onAddClick = { navController.navigate(Routes.AddClient) },
                    onClientClick = { clientId -> navController.navigate(Routes.clientDetail(clientId)) },
                    onOpenSupplierBails = { navController.navigate(Routes.SupplierBails) },
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onOpenSales = { navController.navigate(Routes.Sales) },
                    onGetCreditHistory = getCreditHistory
                )
            }
            
            composable(Routes.AddClient) {
                AddClientScreen(
                    onSave = { name, credit ->
                        clientsViewModel.addClient(name, credit)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            
            composable(Routes.ClientDetail) { backStackEntry ->
                val idArg = backStackEntry.arguments?.getString("clientId")
                val id = idArg?.toIntOrNull()
                val client by (id?.let { clientsViewModel.observeClient(it) } ?: kotlinx.coroutines.flow.flowOf(null)).collectAsState(initial = null)
                val history by (id?.let { clientsViewModel.observeCreditHistory(it) } ?: kotlinx.coroutines.flow.flowOf(emptyList())).collectAsState(initial = emptyList())
                ClientDetailScreen(
                    client = client,
                    onBack = { navController.popBackStack() },
                    creditHistory = history,
                    onApplyCreditChange = { delta, note ->
                        id?.let { clientsViewModel.applyCreditChange(it, delta, note) }
                    },
                    onDeleteClient = {
                        id?.let { clientsViewModel.deleteClient(it) }
                    },
                    onDeleteHistoryEntry = { entry ->
                        clientsViewModel.deleteCreditEntry(entry)
                    },
                    onEditHistoryEntry = { entry, newAmount, newNote ->
                        clientsViewModel.updateCreditEntry(entry, newAmount, newNote)
                    }
                )
            }

            composable(Routes.SupplierBails) {
                val bails by supplierBailsViewModel.bails.collectAsState(initial = emptyList())
                SupplierBailsScreen(
                    bails = bails,
                    onBack = { navController.popBackStack() },
                    onAddBail = { name, amount, photo -> supplierBailsViewModel.addBail(name, amount, photo) },
                    onDeleteBail = { bail -> supplierBailsViewModel.deleteBail(bail) },
                    onOpenBailDetail = { bail -> navController.navigate(Routes.supplierBailDetail(bail.id)) },
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
            
            composable(Routes.SupplierBailDetail) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("bailId")?.toIntOrNull()
                val bail by (id?.let { supplierBailsViewModel.observeBail(it) } ?: kotlinx.coroutines.flow.flowOf(null)).collectAsState(initial = null)
                SupplierBailDetailScreen(
                    bail = bail,
                    onBack = { navController.popBackStack() },
                    onSave = { updated -> supplierBailsViewModel.updateBail(updated) }
                )
            }
            
            composable(Routes.Sales) {
                val context = LocalContext.current
                val db = com.hanouti.app.data.AppDatabase.get(context)
                val repository = com.hanouti.app.data.Repository(db.clientDao(), db.creditEntryDao(), db.supplierBailDao(), db.productDao(), db.salesDao())
                val parentEntry = remember(navController) { navController.getBackStackEntry(Routes.Sales) }
                val vm: SalesViewModel = viewModel(parentEntry, factory = SalesViewModel.Factory(repository))
                SalesScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onOpenScanner = { navController.navigate(Routes.Scanner) },
                    onFinished = { navController.popBackStack() }
                )
            }
            
            composable(Routes.Scanner) {
                val context = LocalContext.current
                val db = com.hanouti.app.data.AppDatabase.get(context)
                val repository = com.hanouti.app.data.Repository(db.clientDao(), db.creditEntryDao(), db.supplierBailDao(), db.productDao(), db.salesDao())
                val parentEntry = try { navController.getBackStackEntry(Routes.Sales) } catch (e: Exception) { null }
                val vm: SalesViewModel = if (parentEntry != null) {
                    viewModel(parentEntry, factory = SalesViewModel.Factory(repository))
                } else {
                    viewModel(factory = SalesViewModel.Factory(repository))
                }
                ScannerScreen(salesViewModel = vm, onBack = { navController.popBackStack() }, onScanned = { navController.popBackStack() })
            }
            
            composable(Routes.History) {
                val db = com.hanouti.app.data.AppDatabase.get(LocalContext.current)
                val repository = com.hanouti.app.data.Repository(db.clientDao(), db.creditEntryDao(), db.supplierBailDao(), db.productDao(), db.salesDao())
                val vm: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory(repository))
                HistoryScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }
            
            composable(Routes.Products) {
                val db = com.hanouti.app.data.AppDatabase.get(LocalContext.current)
                val repository = com.hanouti.app.data.Repository(db.clientDao(), db.creditEntryDao(), db.supplierBailDao(), db.productDao(), db.salesDao())
                val vm: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory(repository))
                ProductsScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }

            composable(Routes.Settings) {
                val activity = LocalContext.current as android.app.Activity
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLanguageChanged = {
                        // Restart the activity to recompose with new locale
                        activity.recreate()
                    }
                )
            }
            
            // QR Code Sync Screens
            composable(Routes.QRGenerator) {
                val context = LocalContext.current
                QRGeneratorScreen(
                    onBack = { navController.popBackStack() },
                    onGenerateQR = {
                        val exporter = com.hanouti.app.data.DatabaseExporter(context)
                        exporter.generateQRData()
                    }
                )
            }
            
            composable(Routes.QRScanner) {
                val context = LocalContext.current
                QRScannerScreen(
                    onBack = { navController.popBackStack() },
                    onQRScanned = { qrData ->
                        val exporter = com.hanouti.app.data.DatabaseExporter(context)
                        val jsonData = exporter.decodeQRData(qrData)
                        jsonData?.let { exporter.importDatabase(it) } ?: false
                    }
                )
            }
        }
    }
}
