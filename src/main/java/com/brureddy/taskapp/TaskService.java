package com.brureddy.taskapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public Task addTask(String title, String description, LocalDate dueDate) {
        Task task = new Task(nextId++, title, description, dueDate);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }

    public Optional<Task> findById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    public boolean updateStatus(int id, TaskStatus status) {
        return findById(id)
                .map(task -> {
                    task.setStatus(status);
                    return true;
                })
                .orElse(false);
    }

    public boolean removeTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public int size() {
        return tasks.size();
    }

    public void replaceAll(List<Task> loadedTasks) {
        tasks.clear();
        tasks.addAll(loadedTasks);
        nextId = loadedTasks.stream()
                .map(Task::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
