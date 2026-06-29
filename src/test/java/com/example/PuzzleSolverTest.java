package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты не зависят от внешнего файла: набор фрагментов передаётся прямо в конструктор.
 * Соединение пазла — последние ДВЕ цифры фрагмента совпадают с первыми ДВУМЯ следующего.
 */
class PuzzleSolverTest {

    private static String solveResult(List<String> fragments) {
        PuzzleSolver solver = new PuzzleSolver(fragments);
        solver.solve();
        return solver.result();
    }

    private static int solveLength(List<String> fragments) {
        PuzzleSolver solver = new PuzzleSolver(fragments);
        solver.solve();
        return solver.chainLength();
    }

    @Test
    @DisplayName("8 фрагментов складываются в одну цепочку из всех 8")
    void allEightFormSingleChain() {
        // Станции 10->20->30->40->50->60->70->80->90, порядок во входе перемешан.
        List<String> fragments = List.of(
                "505560", "101120", "707780", "303340",
                "808890", "202230", "606670", "404450");

        assertEquals(8, solveLength(fragments));
        assertEquals("1011202230334044505560667077808890", solveResult(fragments));
    }

    @Test
    @DisplayName("Из 8 фрагментов самая длинная цепочка — 5, остальные 3 не подходят")
    void longestChainIsSubsetOfEight() {
        // Цепочка 11->22->33->44->55->66 (5 фрагментов) + 3 изолированных.
        List<String> fragments = List.of(
                "110022", "129913", "220033", "778899",
                "330044", "440055", "909091", "550066");

        assertEquals(5, solveLength(fragments));
        assertEquals("1100220033004400550066", solveResult(fragments));
    }

    @Test
    @DisplayName("Ни один из 8 фрагментов не соединяется — цепочка длиной 1")
    void noConnectionsKeepsSingleFragment() {
        List<String> fragments = List.of(
                "100090", "110091", "120092", "130093",
                "140094", "150095", "160096", "170097");

        assertEquals(1, solveLength(fragments));
        assertEquals("100090", solveResult(fragments));
    }

    @Test
    @DisplayName("Официальный пример из условия задачи")
    void taskExample() {
        List<String> fragments = List.of(
                "608017", "248460", "962282", "994725", "177092");

        assertEquals(3, solveLength(fragments));
        assertEquals("24846080177092", solveResult(fragments));
    }
}
