package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PuzzleSolver {

    private final List<String> data;
    private final boolean[] used;

    private final List<Integer> currentPath = new ArrayList<>();
    private List<Integer> bestPath = new ArrayList<>();

    public PuzzleSolver(List<String> fragments) {
        this.data = fragments;
        this.used = new boolean[fragments.size()];
    }

    public void solve() {
        for (int i = 0; i < data.size(); i++) {
            dfs(i);
        }
    }

    public List<Integer> bestPath() {
        return bestPath;
    }

    public int chainLength() {
        return bestPath.size();
    }

    public String result() {
        return buildResult(bestPath);
    }

    private void dfs(int currentIndex) {

        used[currentIndex] = true;
        currentPath.add(currentIndex);

        if (currentPath.size() > bestPath.size()) {
            bestPath = new ArrayList<>(currentPath);
        }

        String currentNumber = data.get(currentIndex);
        String lastTwo = currentNumber.substring(currentNumber.length() - 2);

        for (int next = 0; next < data.size(); next++) {

            if (used[next]) {
                continue;
            }

            String nextNumber = data.get(next);
            String firstTwo = nextNumber.substring(0, 2);

            if (lastTwo.equals(firstTwo)) {
                dfs(next);
            }
        }

        currentPath.remove(currentPath.size() - 1);
        used[currentIndex] = false;
    }

    private String buildResult(List<Integer> path) {

        if (path.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        result.append(data.get(path.get(0)));

        for (int i = 1; i < path.size(); i++) {
            String current = data.get(path.get(i));
            result.append(current.substring(2));
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {

        List<String> data = Files.readAllLines(Path.of("input.txt"));

        PuzzleSolver solver = new PuzzleSolver(data);
        solver.solve();

        System.out.println("Length of chain: " + solver.chainLength());
        System.out.println("Result:");
        System.out.println(solver.result());
    }
}
