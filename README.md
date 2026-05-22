# ProJson - Biblioteca de Geração Json

A **ProJson** é uma biblioteca desenvolvida em Kotlin para a cadeira de Programação Avançada 25/26. 

O objetivo principal é converter qualquer tipo de objetos ou estrutura de dados (coleções) em memória para o formato Json. 
Foi desenhada para lidar dinamicamente com os objetos através de referenciação, em vez da abordagem clássica de árvores hierárquicas. 
Desta forma, é possível eliminar a duplicação dos dados e evitar erros de StackOverflow.

---

## Pré-requisitos:
Para ser possível executar e desenvolver este projeto, é necessário os seguintes requisitos:
- Java Development Kit (JDK): Versão 17 ou superior;
- Kotlin: Versão 1.9+ instalada;
- IDE Recomendado: IntelliJ IDEA.

## Gestão de Dependências:
Este projeto utiliza o Gradle como gestor de dependências. 
É necessário garantir que o ficheiro de configuração inclui o 
módulo de reflexão do Kotlin (o motor do projeto) e o módulo dos testes unitários.

```kotlin 
dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
}
```

## 1. Execução do projeto:
1. Clonar o repositório:

```bash 
git clone https://github.com/anavaln/pa_project.git
cd pa_project
```

2. Importar o projeto para o IDE Intellij.
3. Compilar o projeto, ao exevutar o build do Gradle para verificar se tudo corre como o previsto.
4. Correr os testes unitários para validar o código.

## 2. Arquitetura do projeto:
Esta secção tem como objetivo explicar como o código foi desenvolvido. A arquitetura divide-se na classe central do processamento, o ProJson, 
no módulo de dados estutural (_package models_) e o pacote Utils que serve para cenas gerais do código como as anotações e o módulo de suporte (_Utils_).


#### 2.1. Classe ProJson
A classe ProJson é o motor central da biblioteca. É nesta classe 
que foi desenvolvido o método público principal ``` toJson(obj: Any?) ```, onde é decidido, no tempo de execução, como decompor o objeto que o utilizador enviou.

O funcionamento do motor baseia-se em três etapas lógicas:

A primeira serve para o tratamento dos tipos de objetos introduzidos pelo utilizador. 
Antes de analisar qualquer classe complexa, o motor limpa os casos base imediatos. Se o dado for null, um Número, um Booleano ou uma String, ele é convertido diretamente num JsonPrimitive. Estes tipos são primitivos, o que significa que não pertencem ao mapa de objetos e, por isso, nunca recebem ```$id``` ou ```$type```.

A segunda etapa é relativa à Reflexão (descoberta dinâmica).  
Quando o motor recebe um objeto complexo (como uma Task ou Person), ele usa a API de Reflexão do Kotlin (```kotlin.reflect```) para ler a classe por dentro em tempo de execução. 
O motor extrai dinamicamente todas as propriedades declaradas no objeto e ordena pela variável nome (```sortedBy { it.name }```). Isto garante que a ordem das chaves no JSON final seja sempre a mesma, fazendo com que os assertEquals dos testes unitários funcionem sem falhas aleatórias.

A terceira etapa é o processamento das anotações (os modificadores de comportamento). 
Durante o loop de reflexão, o motor inspeciona se as variáveis ou classes possuem as anotações específicas para alterar o fluxo padrão do JSON. 

#### 2.2. Package Models
Este pacote serve para definir os tipos primitivos de objetos que podem ser dados como input.
- JsonValue (Interface Abstrata): A interface que representa qualquer nó JSON. Define a estrutura para a formatação textual.

- JsonPrimitive: Representa os valores primitivos (String, Number, Boolean, null).

- JsonObject: Representa um objeto JSON. Guarda internamente um mapa de propriedades com a chave e o valor (Map<String, JsonValue>).

- JsonArray: Representa uma lista JSON. Guarda internamente uma coleção ordenada de JsonValue.

#### 2.3. Package Utils
Este módulo é responsável por guardar a classe estrutural MapObjects (que gera os ```$id``` e as ```$ref```) e por armazenar as anotações que funcionam como modificadores de comportamento dos objetos:

@JsonProperty(name): Interceta o fluxo de reflexão. Em vez de usar o nome real da variável no Kotlin, substitui a chave do JsonObject pelo nome customizado definido pelo utilizador.

@JsonIgnore: Ignora completamente a propriedade filtrando-a no loop de reflexão (return@forEach).

@JsonString(Serializer::class): Um mecanismo de plugin. Se uma classe estiver com esta anotação, o ProJson instancia dinamicamente o serializador customizado através de createInstance() e converte o objeto inteiro numa string primitiva.

@Reference: Força explicitamente a propriedade a colapsar num nó de referência ```$ref``` no momento da leitura, delegando o comportamento ao mapObjects.

## 3. Exemplos Práticos 
Nesta secção apresenta-se um cenário de teste real. 

##### 3.1. Declaração das anotações
``` kotlin
 class Task(
        @JsonProperty("desc")
        val description: String,
        @JsonIgnore
        val deadline: Date?,
        @JsonProperty("deps")
        val dependencies: List<Task>
    )
```

##### 3.2. Objeto formatado em Json
O seguinte teste unitário demonstra o comportamento da biblioteca relativamente a uma rede de objetos onde a ordem dos IDs e a geração de referências $ref são validadas de forma estrita

```
    @Test
    fun testTaskWithAnnotations() {
        val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        val t3 = Task("T3", null, listOf(t1, t2))

        val json = ProJson().toJson(listOf(t1, t2, t3))
    }
```

Objeto Json criado!
```
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
```




