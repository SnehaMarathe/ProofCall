package com.proofcall.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PaywallScreen(nav: NavHostController) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Unlock Unlimited Minutes") }) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text("• Unlimited transcripts")
            Text("• Smart highlights")
            Text("• No watermark")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { /* TODO: start purchase flow */ }) {
                Text("₹199/year – Continue")
            }
        }
    }
}
