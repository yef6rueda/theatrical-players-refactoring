# Taller de Refactorización de Theatrical Players (Java)

Este repositorio contiene la solución completa del Taller de Refactorización para la materia de **Reingeniería de Software** (8vo Semestre).

## Backlog de Refactorización Completado

Se siguió un flujo estructurado de micro-pasos seguros manteniendo el comportamiento externo validado por los tests de aprobación en cada paso:

1. **Tarea 3.a (Junior) - Extracción de Créditos:** Extracción de la lógica de créditos por volumen a una función pura llamada `calculateVolumeCredits`.
   * *Commit:* `refactor: extract volume credits calculation into calculateVolumeCredits`
2. **Tarea 3.b (Junior) - Extracción de Montos:** Extracción del cálculo de monto para cada obra a una función independiente llamada `calculateAmount`.
   * *Commit:* `refactor: extract performance amount calculation into calculateAmount`
3. **Tarea 3.c (Intermediate) - Replace Temp with Query:** Eliminación de los acumuladores mutables (`totalAmount` y `volumeCredits`) del bucle principal reemplazándolos por métodos de consulta dedicados (`getTotalAmount` y `getTotalVolumeCredits`).
   * *Commit:* `refactor: replace temp with query to eliminate mutable accumulators from loop`
4. **Tarea 4 (Intermediate/Senior) - Split Phase:** División completa del programa en dos fases: generación de datos estructurados intermediarios (`StatementData` y `PerformanceData` records) y formateo de presentación (`renderPlainText`).
   * *Commit:* `refactor: split phase into statement data generation and text formatting`
5. **Tarea 5 (Senior) - Patrón Strategy / Polimorfismo:** Creación de la jerarquía polimórfica `PlayCalculator` con las subclases `TragedyCalculator` y `ComedyCalculator` para eliminar por completo la condicional `switch`.
   * *Commit:* `refactor: rename PerformanceCalculator to PlayCalculator`
6. **Tarea 5.1 (Nuevo Requisito) - HTML Statement:** Implementación del método `htmlStatement` consumiendo el token intermedio de la fase dividida (`StatementData`), junto con pruebas unitarias de validación en `StatementPrinterTests.java`.
   * *Commit:* `feat: implement htmlStatement using intermediate StatementData`

---

## Ejecución de Pruebas

Para validar el funcionamiento del proyecto de forma local utilizando el wrapper de Gradle:

```bash
cd java
./gradlew test
```

Las pruebas de aprobación (`Approval Tests`) y unitarias se ejecutan de manera exitosa para ambos formatos (texto y HTML).

---

_Original kata description below:_

_Support this and all my katas via [Patreon](https://www.patreon.com/EmilyBache)_

Theatrical Players Refactoring Kata
====================================

The first chapter of ['Refactoring' by Martin Fowler, 2nd Edition](https://www.thoughtworks.com/books/refactoring2) contains a worked example of this exercise, in javascript. That chapter is available to download for free. This repo contains the starting point for this exercise in several languages, with tests, so you can try it out for yourself.

I made a video ["Refactoring with Martin Fowler | Theatrical Players Code Kata"](https://youtu.be/TjIrKEaOiVw) that explains a little bit about the exercise and why you should give it a try.

What you need to change
-----------------------
Refactoring is usually driven by a need to make changes. In the book, Fowler adds code to print the statement as HTML in addition to the existing plain text version. He also mentions that the theatrical players want to add new kinds of plays to their repertoire, for example history and pastoral.

Automated tests
---------------
In his book Fowler mentions that the first step in refactoring is always the same - to ensure you have a solid set of tests for that section of code. However, Fowler did not include the test code for this example in his book. I have used an [Approval testing](https://medium.com/97-things/approval-testing-33946cde4aa8) approach and added some tests. I find Approval testing to be a powerful technique for rapidly getting existing code under test and to support refactoring. You should review these tests and make sure you understand what they cover and what kinds of refactoring mistakes they would expect to find.

Acknowledgements
----------------
Thankyou to Martin Fowler for kindly giving permission to use his code.
