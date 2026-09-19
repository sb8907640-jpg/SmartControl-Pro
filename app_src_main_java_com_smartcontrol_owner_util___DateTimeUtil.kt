package com.smartcontrol.owner.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateTimeUtil {

    private val displayFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val dateOnlyFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatDisplay(date: Date?): String {
        return date?.let { displayFormat.format(it) } ?: "—"
    }

    fun formatDateOnly(date: Date?): String {
        return date?.let { dateOnlyFormat.format(it) } ?: "—"
    }

    fun formatIso(date: Date): String = isoFormat.format(date)

    fun parseIso(iso: String?): Date? {
        return try {
            iso?.let { isoFormat.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun timeAgo(date: Date?): String {
        if (date == null) return "—"

        val diff = System.currentTimeMillis() - date.time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hr ago"
            days < 7 -> "$days days ago"
            else -> formatDateOnly(date)
        }
    }

    fun formatDuration(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
    }
}