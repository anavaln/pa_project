package Utils

import Models.JsonObject
import Models.JsonPrimitive
import java.util.UUID

class MapObjects {

    private val map = mutableMapOf<Any, String>()

    fun contains(obj: Any): Boolean {
        return map.containsKey(obj)
    }

    fun register(obj: Any): String {
        val id = generateId()
        map[obj] = id
        return id
    }

    fun getId(obj: Any): String? {
        return map[obj]
    }

    fun createReference(obj: Any): JsonObject {
        val ref = JsonObject()
        ref.setProperty(
            "\$ref",
            JsonPrimitive(map[obj])
        )
        return ref
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}