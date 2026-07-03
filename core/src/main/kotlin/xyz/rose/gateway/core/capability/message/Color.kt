package xyz.rose.gateway.core.capability.message

data class Color(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    companion object {
        val WHITE = Color(255, 255, 255)

        private val hexRegex = Regex("#([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})")
        private const val RED_INDEX = 1
        private const val GREEN_INDEX = 2
        private const val BLUE_INDEX = 3
        private const val HEX_RADIX = 16

        fun fromHex(hex: String): Color? {
            val matchResult = hexRegex.matchEntire(hex) ?: return null

            val red = matchResult.groupValues[RED_INDEX].toInt(HEX_RADIX)
            val green = matchResult.groupValues[GREEN_INDEX].toInt(HEX_RADIX)
            val blue = matchResult.groupValues[BLUE_INDEX].toInt(HEX_RADIX)

            return Color(red, green, blue)
        }
    }

    val hex: String = "#%02x%02x%02x".format(red, green, blue)
}
