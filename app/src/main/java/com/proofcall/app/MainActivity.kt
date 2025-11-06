package com.proofcall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.proofcall.app.ui.navigation.ProofNavHost
import com.proofcall.app.ui.theme.ProofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProofApp()
        }
    }
}

@Composable
fun ProofApp() {
    ProofTheme {
        val systemUi = rememberSystemUiController()
        SideEffect {
            // Let Material3 handle dynamic colors; no specific colors set here.
        }
        Surface {
            ProofNavHost()
        }
    }
}
