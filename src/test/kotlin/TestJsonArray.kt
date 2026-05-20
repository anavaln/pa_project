import Models.JsonArray
import Models.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class TestJsonArray {

    @Test
    fun testArray() {
        val arr = JsonArray()
        arr.add(JsonPrimitive("a"))
        arr.add(JsonPrimitive(null))
        arr.add(JsonPrimitive("b"))
        arr.add(JsonPrimitive(4))
        val expected = """
[
  "a",
  null,
  "b",
  4
]
""".trimIndent()
        assertEquals(expected, arr.toString())
    }
}