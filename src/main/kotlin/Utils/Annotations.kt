package pt.iscte.pa.projson.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Reference // Para forçar referências de ID ($ref)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class JsonProperty(val name: String) // Mudar a propriedade de um objeto JSON

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class JsonIgnore // Ignorar propriedade na serialização (ex: Date9

@Target(AnnotationTarget.CLASS)
annotation class JsonString(val serializerClass: KClass<out JsonCustomSerializer>) // Transformar/forçar objetos em String

interface JsonCustomSerializer {
    fun serialize(obj: Any): String
}