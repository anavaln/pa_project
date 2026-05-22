package pt.iscte.pa.projson.annotations

import kotlin.reflect.KClass

/**
 * Este módulo define os comportamentos que os utilizadores da bibliotca podem usar,
 * sem a necessidade de modificar o código base das classes.
 *
 * Através destas anotações, é possível alterar as chaves, omitir campos,
 * forçar o colapso de nós em referências ou injetar serializadores customizados.
 */

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Reference

/**
 * Altera o nome da chave que será gerada no `JsonObject` final.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class JsonProperty(val name: String)

/**
 * Atua como um filtro de omissão durante a reflexão.*/
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class JsonIgnore

/**
 * Define um mecanismo de extensão plugin para a serialização de uma classe.
 */
@Target(AnnotationTarget.CLASS)
annotation class JsonString(val serializerClass: KClass<out JsonCustomSerializer>)

/**
 * Contrato de interface obrigatório para a criação de serializadores customizados.
 */
interface JsonCustomSerializer {

    fun serialize(obj: Any): String
}