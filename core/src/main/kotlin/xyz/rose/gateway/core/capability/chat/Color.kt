package xyz.rose.gateway.core.capability.chat

import kotlinx.serialization.Serializable

/**
 * The color of the text in a chat message.
 *
 * @property red The red component of the color.
 * @property green The green component of the color.
 * @property blue The blue component of the color.
 */
@Serializable
data class Color(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    companion object {
        /**
         * White.
         */
        val WHITE = Color(255, 255, 255)

        private val hexRegex = Regex("#([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")
        private const val RED_INDEX = 1
        private const val GREEN_INDEX = 2
        private const val BLUE_INDEX = 3
        private const val HEX_RADIX = 16

        /**
         * Creates a color from a hex string.
         *
         * @param hex The hex string.
         * @return The color or null if the hex string is invalid.
         */
        fun fromHex(hex: String): Color? {
            val matchResult = hexRegex.matchEntire(hex) ?: return null

            val red = matchResult.groupValues[RED_INDEX].toInt(HEX_RADIX)
            val green = matchResult.groupValues[GREEN_INDEX].toInt(HEX_RADIX)
            val blue = matchResult.groupValues[BLUE_INDEX].toInt(HEX_RADIX)

            return Color(red, green, blue)
        }
    }

    /**
     * The hex representation of the color.
     */
    val hex: String = "#%02x%02x%02x".format(red, green, blue)
}
