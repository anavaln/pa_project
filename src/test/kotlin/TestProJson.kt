import kotlin.test.Test
import kotlin.test.assertEquals

class TestProJson {

    data class Task(
        val description: String,
        val deadline: Date?,
        val dependencies: List<Task>
    )
    data class Date(
        val day: Int,
        val month: Int,
        val year: Int
    )

    data class Person(
        val name: String,
        val age: Int,
        val chief: Boolean,
        val dependencies: List<Person>
    )

    @Test
    fun test() {
        //val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        //val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        //val t3 = Task("T3", null, listOf(t1, t2))
        //val json = ProJson().toJson(listOf(t3, t1, t2))
    }

    @Test
    fun testTask() {

        val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        val t3 = Task("T3", null, listOf(t1, t2))

        val json = ProJson().toJson(listOf(t3, t1, t2))

        val expected = """
[
  {
    "${'$'}id": "1",
    "${'$'}type": "Task",
    "deadline": null,
    "dependencies": [
      {
        "${'$'}ref": "2"
      },
      {
        "${'$'}ref": "3"
      }
    ],
    "description": "T3"
  },
  {
    "${'$'}id": "2",
    "${'$'}type": "Task",
    "deadline": {
      "${'$'}id": "4",
      "${'$'}type": "Date",
      "day": 30,
      "month": 2,
      "year": 2026
    },
    "dependencies": [
    ],
    "description": "T1"
  },
  {
    "${'$'}id": "3",
    "${'$'}type": "Task",
    "deadline": {
      "${'$'}id": "5",
      "${'$'}type": "Date",
      "day": 31,
      "month": 4,
      "year": 2026
    },
    "dependencies": [
    ],
    "description": "T2"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }

    @Test
    fun testPerson(){
        val p1 = Person("Anna", 24, true, emptyList())
        val p2 = Person("John", 24, false, listOf(p1))

        val json = ProJson().toJson(listOf(p1, p2))

        println(json)
    }

    @Test
    fun testPerson2() {

        val p1 = Person("Anna", 24, true, emptyList())
        val p2 = Person("John", 24, false, listOf(p1))

        val json = ProJson().toJson(listOf(p1, p2))

        val expected = """
[
  {
    "${'$'}id": "1",
    "${'$'}type": "Person",
    "age": 24,
    "chief": true,
    "dependencies": [
    ],
    "name": "Anna"
  },
  {
    "${'$'}id": "2",
    "${'$'}type": "Person",
    "age": 24,
    "chief": false,
    "dependencies": [
      {
        "${'$'}ref": "1"
      }
    ],
    "name": "John"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }

    @Test
    fun testSimple(){
        val json = ProJson().toJson(listOf("alfabeto", 1))
        println(json)

    }

    @Test
    fun testSimple2() {

        val json = ProJson().toJson(listOf("alfabeto", 1))

        val expected = """
[
  "alfabeto",
  1
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }
}