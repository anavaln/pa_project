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

    /* *
     * Valida a correta interpretação das anotações estruturais e a referenciação numa lista de tarefas.
     * Este teste garante que as propriedades marcadas com @JsonProperty mudam de nome, que os campos com @JsonIgnore
     * são omitidos e que os objetos repetidos geram apenas nós de referência ($ref) usando o ID correto obtido no mapa.
     */
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

    /* *
     * Valida o mecanismo de extensão através de plugins de serialização customizados.
     * Este teste certifica que o motor interpeta a anotação @JsonString ao nível da
     * classe e desvia o processamento padrão por reflexão, transformando o objeto complexo (Date).
     */
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

    /* *
     * Valida o comportamento da anotação @Reference quando aplicada diretamente num campo de relacionamento.
     */
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


    /* *
     * Valida a travessia recursiva padrão e o mapeamento de dependências com tipos de dados idênticos.
     * Este teste garante que objetos complexos do mesmo tipo (Person) geram os metadados de estrutura ($id e $type)
     * e ativam corretamente a minimização automática para referências ($ref) quando detetam um elemento previamente registado.
     */
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

    /* *
     * Valida o fluxo de processamento e isolamento de tipos de dados primitivos.
     * Este teste certifica que o motor interpeta corretamente a Fase 1 do algoritmo
     */
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