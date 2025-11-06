package com.proofcall.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proofcall.app.ui.HomeVm
import com.proofcall.app.ui.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavHostController, vm: HomeVm = viewModel()) {
    val calls by vm.calls.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ProofCall") }, actions = {
                IconButton(onClick = { /* TODO search */ }) { Icon(Icons.Default.Search, contentDescription = "Search") }
                IconButton(onClick = { /* TODO settings */ }) { Icon(Icons.Default.Settings, contentDescription = "Settings") }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate(Routes.RECORD) }) {
                Icon(Icons.Default.Mic, contentDescription = "Record")
            }
        }
    ) { padding ->
        if (calls.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No calls yet. Tap the mic to start.")
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(calls) { call ->
                    ListItem(
                        headlineContent = { Text(call.title) },
                        supportingContent = {
                            val sdf = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
                            Text("${sdf.format(Date(call.startedAt))} • ${call.durationSec/60}m")
                        },
                        trailingContent = {
                            TextButton(onClick = { nav.navigate("detail/${call.id}") }) {
                                Text("Summary")
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
