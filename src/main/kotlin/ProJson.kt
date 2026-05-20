import Utils.MapObjects
import Models.*
import pt.iscte.pa.projson.annotations.*
import kotlin.reflect.full.*

class ProJson {
    private val mapObjects = MapObjects()

    fun toJson(obj: Any?): JsonValue {
        if (obj == null) return JsonPrimitive(null)

        if (obj is Number || obj is Boolean || obj is String) {
            return JsonPrimitive(obj)
        }

        if (obj is Iterable<*>) {
            val jsonArray = JsonArray()
            obj.forEach { jsonArray.add(toJson(it)) }
            return jsonArray
        }

        if (obj is Map<*, *>) {
            val jsonObject = JsonObject()
            obj.forEach { (key, value) ->
                jsonObject.setProperty(key.toString(), toJson(value))
            }
            return jsonObject
        }

        // Reflection
        val kClass = obj::class

        if (mapObjects.contains(obj)) {
            return mapObjects.createReference(obj)
        }

        val currentId = mapObjects.register(obj)

        val jsonObject = JsonObject()
        jsonObject.setProperty("\$id", JsonPrimitive(currentId))
        jsonObject.setProperty("\$type", JsonPrimitive(kClass.simpleName ?: "Unknown"))

        // anotação do @JsonString
        val jsonStringAnno = kClass.findAnnotation<JsonString>()
        if (jsonStringAnno != null) {
            val serializer = jsonStringAnno.serializerClass.createInstance()
            return JsonPrimitive(serializer.serialize(obj))
        }

        val properties = kClass.memberProperties.sortedBy { it.name }

        properties.forEach { prop ->
            val propValue = prop.call(obj)

            // anotação do @JsonIgnore
            if (prop.hasAnnotation<JsonIgnore>()) {
                return@forEach
            }

            // anotação do @JsonProperty
            val jsonPropertyAnno = prop.findAnnotation<JsonProperty>()
            val jsonKey = jsonPropertyAnno?.name ?: prop.name

            if (propValue == null) {
                jsonObject.setProperty(jsonKey, JsonPrimitive(null))
                return@forEach
            }

            // anotação do @Reference
            if (prop.hasAnnotation<Reference>()) {
                if (propValue is Iterable<*>) {
                    val refArray = JsonArray()
                    propValue.forEach { item ->
                        if (item != null) {
                            if (!mapObjects.contains(item)) mapObjects.register(item)
                            refArray.add(mapObjects.createReference(item))
                        }
                    }
                    jsonObject.setProperty(jsonKey, refArray)
                } else {
                    if (!mapObjects.contains(propValue)) mapObjects.register(propValue)
                    jsonObject.setProperty(jsonKey, mapObjects.createReference(propValue))
                }
            } else {
                jsonObject.setProperty(jsonKey, toJson(propValue))
            }
        }

        return jsonObject
    }
}