package Models

import kotlin.collections.iterator

class JsonObject : JsonValue {
    val properties = mutableMapOf<String, JsonValue>()

    fun setProperty(key: String, value: JsonValue) {
        properties[key] = value
    }

    fun getProperty(key: String): JsonValue? {
        return properties[key]
    }

    fun removeProperty(key: String) {
        properties.remove(key)
    }

    fun keys(): Set<String> {
        return properties.keys
    }

    override fun toString(): String {
        return toString(0)
    }

    fun toString(indent: Int): String {
        return toJson(indent, "{", "}", properties.map { it.key to it.value })
    }
}
