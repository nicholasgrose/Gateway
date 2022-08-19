package com.rose.gateway.shared.error

/**
 * Asserts that the value is not null, raising a custom error message, if it is.
 * This is meant as a more informative alternative to non-null assertions.
 *
 * @param T The type to guarantee the value is.
 * @param failureMessage The error to raise if the value is null.
 * @return The non-nullable type.
 */
fun <T> T?.notNull(failureMessage: String): T {
    return this ?: error(failureMessage)
}
