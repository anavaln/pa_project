package Models

interface JsonValue {
    override fun toString(): String

    fun toJson(
        indent: Int,
        openBracket: String,
        closeBracket: String,
        values: Iterable<Pair<String?, JsonValue>>
    ): String {
        val items = values.toList()
        val sb = StringBuilder()
        val indentStr = "  ".repeat(indent)

        sb.append(openBracket)
        sb.append("\n")

        var first = true
        for ((key, value) in items) {
            if (!first) {
                sb.append(",\n")
            }
            first = false

            sb.append(indentStr)
            sb.append("  ")

            if (key != null) {
                sb.append("\"")
                sb.append(key)
                sb.append("\": ")
            }

            sb.append(value.toPrettyString(indent + 1))
        }

        if (items.isNotEmpty()) {
            sb.append("\n")
        }
        sb.append(indentStr)
        sb.append(closeBracket)

        return sb.toString()
    }

    private fun JsonValue.toPrettyString(indent: Int): String {
        return when (this) {
            is JsonObject -> toString(indent)
            is JsonArray -> toString(indent)
            else -> toString()
        }
    }
}
