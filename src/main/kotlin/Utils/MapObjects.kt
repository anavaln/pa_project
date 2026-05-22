package Utils

import Models.JsonObject
import Models.JsonPrimitive

/**
 * Atua como um mapa de objetos, sendo responsável por mapear instâncias na memória para identificadores numéricos sequenciais únicos e criar nós de referência (`$ref`).
 */

class MapObjects {

    private val map = mutableMapOf<Any, String>()
    private var counter = 1

    fun contains(obj: Any): Boolean {
        return map.containsKey(obj)
    }

    fun register(obj: Any): String {
        val id = generateId()
        map[obj] = id
        return id
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
        return counter++.toString()
    }

    override fun toString(): String {
        return map.toString()
    }
}