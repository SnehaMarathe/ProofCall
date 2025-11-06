package com.proofcall.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import java.util.UUID

@Entity(tableName = "calls")
@TypeConverters(Converters::class)
data class Call(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val startedAt: Long,
    val durationSec: Int,
    val highlights: List<Int> = emptyList(),
    val confidence: Int = 0,
    val transcript: List<TranscriptLine> = emptyList(),
    val summary: List<String> = emptyList(),
    val sharedAt: Long? = null,
    val tags: List<String> = emptyList(),
    val premiumRequired: Boolean = false
)

data class TranscriptLine(
    val t: Int,
    val text: String,
    val isHighlight: Boolean = false
)

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun toIntList(csv: String): List<Int> = if (csv.isBlank()) emptyList() else csv.split(",").map { it.toInt() }

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString("||")

    @TypeConverter
    fun toStringList(s: String): List<String> = if (s.isBlank()) emptyList() else s.split("||")

    @TypeConverter
    fun fromTranscript(list: List<TranscriptLine>): String =
        list.joinToString("§") { "${it.t}::${it.text}::${it.isHighlight}" }

    @TypeConverter
    fun toTranscript(s: String): List<TranscriptLine> =
        if (s.isBlank()) emptyList()
        else s.split("§").map {
            val parts = it.split("::")
            TranscriptLine(
                t = parts.getOrNull(0)?.toIntOrNull() ?: 0,
                text = parts.getOrNull(1) ?: "",
                isHighlight = parts.getOrNull(2)?.toBooleanStrictOrNull() ?: false
            )
        }
}
