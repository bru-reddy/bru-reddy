package com.brureddy.taskapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private static final String SEPARATOR = "|";

    public List<Task> load(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            return List.of();
        }

        List<Task> loaded = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            String[] parts = line.split("\\\\|", -1);
            if (parts.length != 5) {
                continue;
            }
            int id = Integer.parseInt(parts[0]);
            String title = unescape(parts[1]);
            String description = unescape(parts[2]);
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            LocalDate dueDate = LocalDate.parse(parts[4]);

            Task task = new Task(id, title, description, dueDate);
            task.setStatus(status);
            loaded.add(task);
        }

        return loaded;
    }

    public void save(Path filePath, List<Task> tasks) throws IOException {
        List<String> lines = tasks.stream()
                .map(task -> String.join(SEPARATOR,
                        String.valueOf(task.getId()),
                        escape(task.getTitle()),
                        escape(task.getDescription()),
                        task.getStatus().name(),
                        task.getDueDate().toString()))
                .toList();

        Files.write(filePath, lines);
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace(SEPARATOR, "\\|");
    }

    private String unescape(String text) {
        return text.replace("\\|", SEPARATOR).replace("\\\\", "\\");
    }
}
