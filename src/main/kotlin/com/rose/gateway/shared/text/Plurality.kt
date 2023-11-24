package com.rose.gateway.shared.text

/**
 * Determines whether to use the singular or plural for a count
 *
 * @param count The number of items
 * @param singular The singular form
 * @param plural The plural form
 * @return The form corresponding to the count
 */
fun plurality(
    count: Int,
    singular: String,
    plural: String,
): String =
    when (count) {
        1 -> singular
        else -> plural
    }
