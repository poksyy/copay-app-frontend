package com.copay.app.utils

import java.text.SimpleDateFormat
import java.util.*

// Formats a raw date string (from backend) into a more readable format.
fun formatDateTime(raw: String, format: String = "dd MMM, HH:mm"): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = parser.parse(raw)
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.format(date ?: Date())
    } catch (e: Exception) {
        raw
    }
}

// Parses a date string into a Date object.
fun parseConfirmationDate(dateString: String): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        format.parse(dateString)
    } catch (e: Exception) {
        null
    }
}
