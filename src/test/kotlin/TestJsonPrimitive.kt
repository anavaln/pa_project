import Models.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class TestJsonPrimitive {

    @Test
    fun testStringPrimitive() {
        val primitive = JsonPrimitive("Ana Valente")
        assertEquals("\"Ana Valente\"", primitive.toString())
    }

    @Test
    fun testNumberPrimitive() {
        val primitive = JsonPrimitive(4)
        assertEquals("4", primitive.toString())
    }

    @Test
    fun testBooleanPrimitive() {
        val primitive = JsonPrimitive(true)
        assertEquals("true", primitive.toString())
    }

    @Test
    fun testNullPrimitive() {
        val primitive = JsonPrimitive(null)
        assertEquals("null", primitive.toString())
    }
}