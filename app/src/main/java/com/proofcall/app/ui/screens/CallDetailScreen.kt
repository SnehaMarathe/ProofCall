
package com.proofcall.app.ui.screens

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proofcall.app.ui.DetailVm
import com.proofcall.app.util.ShareUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share

@Composable
fun CallDetailScreen(nav: NavHostController, callId: String, vm: DetailVm = viewModel()) {
    val call by vm.callFlow(callId).collectAsState(initial = null)
    val ctx = LocalContext.current
    var showShare by remember { mutableStateOf(false) }

    val sdf = remember { SimpleDateFormat("MMM d, yyyy • HH:mm", Locale.getDefault()) }
    val startedAtStr = remember(call) { call?.let { sdf.format(Date(it.startedAt)) } ?: "" }
    val durationStr = remember(call) { call?.let { DateUtils.formatElapsedTime(it.durationSec.toLong()) } ?: "" }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(call?.title ?: "Details") },
                navigationIcon = { IconButton({ nav.popBackStack() }) { Icon(Icons.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { showShare = true }) { Icon(Icons.Filled.Share, null) }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (call == null) {
                Text("Loading...")
            } else {
                Text("Minutes", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                call!!.summary.forEach { Text("• $it") }
                Spacer(Modifier.height(24.dp))
                Text("Transcript", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                call!!.transcript.forEach { Text("[${it.t}s] ${it.text}") }
            }
        }
    }

    if (showShare && call != null) {
        AlertDialog(
            onDismissRequest = { showShare = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                Column(Modifier.fillMaxWidth()) {
                    Text("Share Minutes via")
                    Spacer(Modifier.height(12.dp))
                    val text = ShareUtils.buildMinutesText(
                        title = call!!.title,
                        startedAtStr = startedAtStr,
                        durationStr = durationStr,
                        bullets = call!!.summary
                    )
                    Button(
                        onClick = {
                            ShareUtils.shareToWhatsApp(ctx, text)
                            showShare = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("WhatsApp") }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            ShareUtils.shareToSms(ctx, text)
                            showShare = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("SMS") }
                }
            },
            title = { Text("Share Minutes") }
        )
    }
}
