# pa_project

## ProJson - Biblioteca de Geração JSON com Referências

[cite_start]A **ProJson** é uma biblioteca Kotlin leve e extensível desenvolvida para converter estruturas de dados complexas em formato textual JSON[cite: 1, 2]. [cite_start]O projeto foca-se na transparência para o utilizador, permitindo que objetos em memória sejam serializados sem a necessidade de gestão manual de identificadores ou conversões de tipos[cite: 73, 74].

[cite_start]Este projeto foi realizado no âmbito da unidade curricular de **Programação Avançada 2025/2026**.

---

## 🚀 Funcionalidades (Fase 1: Standard JSON)

[cite_start]Nesta primeira fase, a biblioteca foca-se no modelo em memória e na serialização básica[cite: 84, 85]:

* [cite_start]**Modelo de Objetos In-Memory**: Implementação de uma hierarquia baseada em `JsonValue`, incluindo `JsonObject`, `JsonArray` e `JsonPrimitive`[cite: 76, 77, 78, 81].
* [cite_start]**Geração via Reflexão**: Capacidade de gerar objetos JSON automaticamente a partir de qualquer instância de classe em memória, extraindo as suas propriedades sem intervenção do utilizador[cite: 87, 90].
* [cite_start]**Identificação de Tipos**: Inclusão automática da propriedade `$type` em cada objeto JSON para indicar a classe de origem[cite: 37].
* [cite_start]**Manipulação Dinâmica**: API completa para leitura, escrita, adição e remoção de propriedades em tempo de execução[cite: 98, 100].
* [cite_start]**Pretty-Print (Opcional)**: Geração de texto JSON válido e formatado[cite: 102, 103].

---

## 🛠️ Como Utilizar

### 1. Definição de Classes
[cite_start]A biblioteca suporta classes Kotlin padrão e `data classes`[cite: 13, 18].

```kotlin
data class Date(val day: Int, val month: Int, val year: Int)

class Task(
    val description: String,
    val deadline: Date?,
    val dependencies: List<Task>
)
