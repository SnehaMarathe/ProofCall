
package com.proofcall.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object ShareUtils {

    fun shareToWhatsApp(context: Context, text: String) {
        val waIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(waIntent)
        } catch (e: ActivityNotFoundException) {
            val chooser = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }, "Share Minutes")
            context.startActivity(chooser)
        }
    }

    fun shareToSms(context: Context, text: String) {
        val smsUri = Uri.parse("smsto:")
        val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", text)
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No SMS app found.", Toast.LENGTH_SHORT).show()
        }
    }

    fun buildMinutesText(
        title: String,
        startedAtStr: String,
        durationStr: String,
        bullets: List<String>,
        footer: String = "— Sent via ProofCall"
    ): String {
        val sb = StringBuilder()
        sb.appendLine("Minutes of Call: $title")
        sb.appendLine("When: $startedAtStr • Duration: $durationStr")
        sb.appendLine()
        bullets.forEachIndexed { idx, b -> sb.appendLine("${idx + 1}. $b") }
        sb.appendLine()
        sb.append(footer)
        return sb.toString()
    }
}
