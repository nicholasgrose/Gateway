package com.rose.gateway.minecraft.logging

import java.io.PrintWriter
import java.io.StringWriter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.LogManager
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

class LogFormatter : SimpleFormatter() {

   val format = "%s: %s [%s]%n"

    override fun format(record: LogRecord): String? {
        val zdt = ZonedDateTime.ofInstant(
            record.instant, ZoneId.systemDefault()
        )
        val message = formatMessage(record)
        return String.format(
            format,
            record.level.localizedName,
            message,
            zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }
}