import Utils.MapObjects
import Models.*
import pt.iscte.pa.projson.annotations.*
import kotlin.reflect.full.*

/* *
 * Esta classe representa o motor de conversão central da biblioteca, sendo responsável por
 * gerir o ciclo de vida da serialização através do mapeamento de referências e reflexão.
 */

class ProJson {
    private val mapObjects = MapObjects()

    fun toJson(obj: Any?): JsonValue {
        /* * Fase 1: Tratamento dos casos primitivos e estruturas de dados
        * Esta fase limpa de imediato os tipos de dados.
        * Também processa as coleções estruturais iteráveis e mapas recursivamente.
        */
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

        /* * Fase 2: Reflexão
         * Alcançada apenas por objetos estruturais complexos de classes do utilizador.
         * Utiliza a API 'kotlin.reflect' para inspecionar o objeto em tempo de execução.
         * Valida-se se o objeto já foi registado antes, colapsa-o num
         * nó de referência ($ref); caso contrário, atribui-lhe um novo ID único,
         * inicializa o JsonObject e injeta $id e $type.
         */
        val kClass = obj::class

        if (mapObjects.contains(obj)) {
            return mapObjects.createReference(obj)
        }

        val currentId = mapObjects.register(obj)

        val jsonObject = JsonObject()
        jsonObject.setProperty("\$id", JsonPrimitive(currentId))
        jsonObject.setProperty("\$type", JsonPrimitive(kClass.simpleName ?: "Unknown"))

        /* * Fase 3: Anotações
         * O motor itera sobre todos os campos declarados na classe do objeto,
         * ordenados alfabeticamente.
         * Durante o ciclo, lê os valores de cada propriedade e aplica os modificadores
         * de comportamento (@JsonIgnore, @JsonProperty @JsonString e @Reference),
         * decidindo se a propriedade avança, se modifica o nome da chave, se escolhe uma
         * estrutura serializada customizada ou se é forçada a usar a referência.
         */

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