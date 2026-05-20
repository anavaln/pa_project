import Utils.MapObjects
import Models.*
import pt.iscte.pa.projson.annotations.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProJson {

    class Task(
        @JsonProperty("desc")
        val description: String,
        @JsonIgnore
        val deadline: Date?,
        @JsonProperty("deps")
        val dependencies: List<Task>
    )

    @JsonString(DateAsText::class)
    data class Date(
        val day: Int,
        val month: Int,
        val year: Int
    )

    class DateAsText : JsonCustomSerializer {
        override fun serialize(obj: Any): String {
            val date = obj as Date
            return String.format("%02d/%02d/%d", date.day, date.month, date.year)
        }
    }

    data class Person(
        val name: String,
        val age: Int,
        val chief: Boolean,
        val dependencies: List<Person>
    )

    data class Course(
        val title: String,
        @Reference
        val instructor: Person
    )

    // test1
    @Test
    fun testTaskWithAnnotations() {
        val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        val t3 = Task("T3", null, listOf(t1, t2))

        val json = ProJson().toJson(listOf(t1, t2, t3))

        val expected = """
[
  {
    "${'$'}id": "1",
    "${'$'}type": "Task",
    "deps": [
    ],
    "desc": "T1"
  },
  {
    "${'$'}id": "2",
    "${'$'}type": "Task",
    "deps": [
    ],
    "desc": "T2"
  },
  {
    "${'$'}id": "3",
    "${'$'}type": "Task",
    "deps": [
      {
        "${'$'}ref": "1"
      },
      {
        "${'$'}ref": "2"
      }
    ],
    "desc": "T3"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }


    // test2
    @Test
    fun testTaskReverse() {
        val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        val t3 = Task("T3", null, listOf(t1, t2))

        val json = ProJson().toJson(listOf(t3, t1, t2))

        val expected = """
[
  {
    "${'$'}id": "3",
    "${'$'}type": "Task",
    "deps": [
      {
        "${'$'}ref": "1"
      },
      {
        "${'$'}ref": "2"
      }
    ],
    "desc": "T3"
  },
  {
    "${'$'}id": "1",
    "${'$'}type": "Task",
    "deps": [
    ],
    "desc": "T1"
  },
  {
    "${'$'}id": "2",
    "${'$'}type": "Task",
    "deps": [
    ],
    "desc": "T2"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }

    // test3
    @Test
    fun testJsonStringPlugin() {
        val d1 = Date(30, 2, 2026)
        val d2 = Date(31, 4, 2026)

        val json = ProJson().toJson(listOf(d1, d2))

        val expected = """
[
  "30/02/2026",
  "31/04/2026"
]
""".trimIndent()

        assertEquals(expected, json.toString())
        println(json.toString())
    }

    // test4
    @Test
    fun testExplicitReferenceAnnotation() {
        val teacher = Person("Ana", 24, true, emptyList())
        val course = Course("Matemática", teacher)

        val json = ProJson().toJson(listOf(teacher, course))

        val expected = """
[
  {
    "${'$'}id": "1",
    "${'$'}type": "Person",
    "age": 24,
    "chief": true,
    "dependencies": [
    ],
    "name": "Ana"
  },
  {
    "${'$'}id": "2",
    "${'$'}type": "Course",
    "instructor": {
      "${'$'}ref": "1"
    },
    "title": "Matemática"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }


    // test5
    @Test
    fun testPerson() {
        val p1 = Person("Ana", 24, true, emptyList())
        val p2 = Person("Maria", 24, false, listOf(p1))

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
    "name": "Ana"
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
    "name": "Maria"
  }
]
""".trimIndent()

        assertEquals(expected, json.toString())
    }

    // test6
    @Test
    fun testSimplePrimitives() {
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