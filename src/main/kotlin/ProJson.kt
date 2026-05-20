import Models.JsonArray
import Models.JsonObject
import Models.JsonPrimitive
import Models.JsonValue
import Utils.MapObjects
import java.util.UUID
import kotlin.reflect.full.memberProperties

class ProJson {
    // mapa que serve para guardar os objetos visitados
    private val visited = MapObjects()

    // função principal, a função que é chamada pelo utilizador
    fun toJson(obj: Any?): JsonValue {
        return convert(obj)
    }

    // função que converte qualquer objeto para Models.JsonValue
    fun convert(obj: Any?): JsonValue {
        if (obj == null) return JsonPrimitive(null)
        if (obj is String || obj is Number || obj is Boolean) return JsonPrimitive(obj)
        if (obj is Iterable<*>) {
            val arrayJson = JsonArray()
            obj.forEach { i -> arrayJson.add(toJson(i)) }
            return arrayJson
        }

        visited.createReference(obj)
        val id = visited.register(obj)

        // reflection
        obj::class.memberProperties

        val jsonObject = JsonObject()


        val properties = obj::class.memberProperties

        for (property in properties) {
            val name = property.name
            val value = property.getter.call(obj)

            jsonObject.setProperty(name, convert(value))
        }
        return jsonObject
    }
}