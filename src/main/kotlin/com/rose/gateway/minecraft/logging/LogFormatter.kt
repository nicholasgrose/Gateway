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

    override fun format(record: LogRecord): String? {
        val zdt = ZonedDateTime.ofInstant(
            record.instant, ZoneId.systemDefault()
        )
        val message = formatMessage(record)

        return "${record.level.localizedName}: $message " +
            "${zdt.format(DateTimeFormatter.ofPattern("EEE, yyyy-MM-dd HH:mm:ss"))} \n"
    }
}
