import Models.JsonObject
import Models.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class TestJsonObject {

    @Test
    fun testObject() {
        val obj = JsonObject()
        obj.setProperty("name", JsonPrimitive("Ana Valente"))
        obj.setProperty("age", JsonPrimitive(25))
        val expected = """
{
  "name": "Ana Valente",
  "age": 25
}
""".trimIndent()
        assertEquals(expected, obj.toString())
        println(obj)
    }
}