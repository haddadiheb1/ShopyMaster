package com.hanouti.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import com.hanouti.app.settings.LanguagePreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit, onLanguageChanged: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf("en") }

    LaunchedEffect(Unit) {
        selected = LanguagePreferences.getLanguage(context)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(id = com.hanouti.app.R.string.settings)) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }
        )
    }) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).padding(16.dp)) {
            Text(text = stringResource(id = com.hanouti.app.R.string.language), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.padding(4.dp))
            val options = listOf(
                "en" to stringResource(id = com.hanouti.app.R.string.lang_english),
                "ar" to stringResource(id = com.hanouti.app.R.string.lang_arabic),
                "fr" to stringResource(id = com.hanouti.app.R.string.lang_french),
            )
            options.forEach { (code, label) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected == code, onClick = { selected = code })
                    Text(text = label, modifier = Modifier.padding(start = 8.dp))
                }
            }
            Spacer(Modifier.padding(8.dp))
            Button(onClick = {
                scope.launch {
                    LanguagePreferences.setLanguage(context, selected)
                    onLanguageChanged()
                }
            }) { Text(text = stringResource(id = com.hanouti.app.R.string.apply_and_restart)) }
        }
    }
}


