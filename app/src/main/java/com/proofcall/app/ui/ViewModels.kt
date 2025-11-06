package com.proofcall.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proofcall.app.data.Call
import com.proofcall.app.data.TranscriptLine
import com.proofcall.app.di.ServiceLocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.max

class HomeVm(app: Application) : AndroidViewModel(app) {
    private val repo = ServiceLocator.repository(app)
    val calls: StateFlow<List<Call>> = repo.calls()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class RecordingVm(app: Application) : AndroidViewModel(app) {
    private val repo = ServiceLocator.repository(app)
    private var startTime: Long = 0L
    private var buffer = mutableListOf<TranscriptLine>()
    private var highlights = mutableListOf<Int>()

    fun start() {
        startTime = System.currentTimeMillis()
        buffer.clear()
        highlights.clear()
        // TODO: start AudioRecord + on-device STT; append lines with timestamps
    }

    fun addLine(seconds: Int, text: String, highlight: Boolean = false) {
        buffer.add(TranscriptLine(t = seconds, text = text, isHighlight = highlight))
        if (highlight) highlights.add(seconds)
    }

    fun toggleHighlight(seconds: Int) {
        val idx = buffer.indexOfFirst { it.t == seconds }
        if (idx >= 0) {
            val current = buffer[idx]
            val toggled = current.copy(isHighlight = !current.isHighlight)
            buffer[idx] = toggled
            if (toggled.isHighlight) highlights.add(seconds) else highlights.remove(seconds)
        }
    }

    fun stopAndSave(title: String = "Call") {
        val durSec = max(1, ((System.currentTimeMillis() - startTime) / 1000L).toInt())
        val minutesSummary = summarize(buffer)
        val call = Call(
            title = title,
            startedAt = startTime,
            durationSec = durSec,
            highlights = highlights.toList(),
            confidence = 92, // TODO compute real confidence
            transcript = buffer.toList(),
            summary = minutesSummary
        )
        viewModelScope.launch { repo.save(call) }
    }

    private fun summarize(lines: List<TranscriptLine>): List<String> {
        // Simple heuristic summary (placeholder)
        val bullets = mutableListOf<String>()
        lines.take(5).forEach { bullets.add(it.text.take(80)) }
        return if (bullets.isEmpty()) listOf("No content captured.") else bullets
    }
}

class DetailVm(app: Application) : AndroidViewModel(app) {
    private val repo = ServiceLocator.repository(app)
    fun callFlow(id: String) = repo.call(id)
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }
}
