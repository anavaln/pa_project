package Models

class JsonPrimitive(val value: Any?) : JsonValue {

    override fun toString(): String {
        if (value is String) return "\"" + value + "\""
        if (value is Number) return value.toString()
        if (value is Boolean) return value.toString()
        return "null"
    }
}
