package com.proofcall.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proofcall.app.ui.RecordingVm
import com.proofcall.app.ui.navigation.Routes

@Composable
fun RecordingScreen(nav: NavHostController, vm: RecordingVm = viewModel()) {
    var running by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }
    val transcript = remember { mutableStateListOf<com.proofcall.app.data.TranscriptLine>() }

    LaunchedEffect(running) {
        if (running) {
            vm.start()
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Recording") }) },
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    // Mark highlight at current seconds
                    transcript.add(com.proofcall.app.data.TranscriptLine(seconds, "★ Highlight", true))
                    vm.toggleHighlight(seconds)
                }) { Text("★ Highlight") }
                if (running) {
                    Button(onClick = {
                        running = false
                        vm.stopAndSave()
                        nav.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = false } }
                    }) { Text("Stop") }
                } else {
                    Button(onClick = { running = true }) { Text("Start") }
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Text("Live transcript", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp)) {
                items(transcript) { line ->
                    Text("[${line.t}s] ${line.text}")
                }
            }
            // TODO: Hook to real AudioRecord + STT and push to 'transcript'
        }
    }
}
