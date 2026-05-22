package Models

/**
 * Representa uma lista ordenada de valores JSON (`[]`) que serve ara armazenar e formatar coleções de dados de forma hierárquica.
 */

class JsonArray : JsonValue {
    val elements = mutableListOf<JsonValue>()

    fun add(value: JsonValue) {
        elements.add(value)
    }

    fun get(index: Int): JsonValue {
        return elements[index]
    }

    fun removeAt(index: Int) {
        elements.removeAt(index)
    }

    fun size(): Int = elements.size

    override fun toString(): String {
        return toString(0)
    }

    fun toString(indent: Int): String {
        return toJson(indent, "[", "]", elements.map { null to it })
    }
}
