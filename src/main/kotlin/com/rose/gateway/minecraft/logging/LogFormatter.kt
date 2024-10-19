package com.rose.gateway.minecraft.logging

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

/**
 * This class determines the format of the FileLogger
 */
class LogFormatter : SimpleFormatter() {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd HH:mm:ss")

    override fun format(record: LogRecord): String {
        val zdt =
            ZonedDateTime.ofInstant(
                record.instant,
                ZoneId.systemDefault(),
            )
        val message = formatMessage(record)
        val warningLevel = record.level.localizedName

        return "$warningLevel: $message ${zdt.format(dateTimeFormatter)}\n"
    }
}
