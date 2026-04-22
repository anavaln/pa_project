# pa_project

## ProJson - Biblioteca de Geração JSON com Referências

A **ProJson** é uma biblioteca Kotlin leve e extensível desenvolvida para converter estruturas de dados complexas em formato textual JSON. O projeto foca-se na transparência para o utilizador, permitindo que objetos em memória sejam serializados sem a necessidade de gestão manual de identificadores ou conversões de tipos.

Este projeto foi realizado no âmbito da unidade curricular de **Programação Avançada 2025/2026**.

---

## Fase 1: Standard JSON

Nesta primeira fase, a biblioteca foca-se no modelo em memória e na serialização básica:

**Modelo de Objetos In-Memory**: Implementação de uma interface 'JsonValue', que é a base para todos os objetos Json. Esta interface é hierarquizada para as classes 'JsonObject' (mapas - objetos com um par de chave e valor), 'JsonArray' (objetos para coleções - listas, sets) e 'JsonPrimitive' (obtevos para os tipos primitivos - String, Inteiro e Boolean).

**Geração via Reflexão**: Capacidade de gerar objetos JSON automaticamente a partir de qualquer instância de classe em memória, extraindo as suas propriedades sem intervenção do utilizador.

**Identificação de Tipos**: Inclusão automática da propriedade `$type` em cada objeto JSON para indicar a classe de origem.

**Manipulação Dinâmica**: API completa para leitura, escrita, adição e remoção de propriedades em tempo de execução.


## 2ª Fase: Referências e Identificadores
Nesta fase, o objetivo é resolver o problema dos "grafos", ou seja, mostrar as dependências que certos objetos têm de outros objetos.

**Identificação Automática ($id)**: A biblioteca passará a atribuir um UUID (um identificador único) a cada objeto complexo.

**Referências ($ref)**: Se o mesmo objeto for encontrado outra vez na árvore, a biblioteca não o repete. Em vez disso, coloca apenas uma referência.

**Anotação**: Vais criar uma anotação para marcar que propriedades devem ser tratadas como referências, evitando que o JSON fique gigante ou entre em loop infinito.

## 3ª Fase: Documentação
Esta fase foca-se em criar a documentação da API.
