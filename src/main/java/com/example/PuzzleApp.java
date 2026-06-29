package com.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PuzzleApp extends Application {

    private final TextField filePathField = new TextField("input.txt");
    private final TextArea outputArea = new TextArea();
    private final Label statusLabel = new Label();

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        Button browseButton = new Button("Выбрать файл…");
        browseButton.setOnAction(event -> chooseFile());

        Label fileLabel = new Label("Файл:");
        HBox fileRow = new HBox(8, fileLabel, filePathField, browseButton);
        HBox.setHgrow(filePathField, Priority.ALWAYS);

        Button solveButton = new Button("Ответ");
        solveButton.setMaxWidth(Double.MAX_VALUE);
        solveButton.setOnAction(event -> solve(solveButton));

        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        VBox root = new VBox(10, fileRow, solveButton, statusLabel, outputArea);
        root.setPadding(new Insets(16));

        Scene scene = new Scene(root, 600, 420);
        primaryStage.setTitle("Цифровий пазл");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл с фрагментами");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));

        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    private void solve(Button solveButton) {
        String path = filePathField.getText().trim();
        outputArea.clear();
        statusLabel.setText("Вычисление…");
        solveButton.setDisable(true);

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                List<String> data = Files.readAllLines(Path.of(path));
                PuzzleSolver solver = new PuzzleSolver(data);
                solver.solve();
                return "Длина цепочки: " + solver.chainLength()
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + solver.result();
            }
        };

        task.setOnSucceeded(event -> {
            outputArea.setText(task.getValue());
            statusLabel.setText("Готово");
            solveButton.setDisable(false);
        });
        task.setOnFailed(event -> {
            Throwable error = task.getException();
            outputArea.setText("Ошибка: " + (error == null ? "неизвестно" : error.getMessage()));
            statusLabel.setText("Ошибка");
            solveButton.setDisable(false);
        });

        Thread worker = new Thread(task, "puzzle-solver");
        worker.setDaemon(true);
        worker.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
