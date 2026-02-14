package com.brureddy.taskapp;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Path STORAGE_PATH = Path.of("tasks.db");

    public static void main(String[] args) {
        TaskService service = new TaskService();
        TaskStorage storage = new TaskStorage();

        try {
            service.replaceAll(storage.load(STORAGE_PATH));
        } catch (IOException e) {
            System.out.println("Could not load saved tasks: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> addTask(scanner, service);
                    case "2" -> listTasks(service);
                    case "3" -> updateTaskStatus(scanner, service);
                    case "4" -> removeTask(scanner, service);
                    case "5" -> {
                        saveAndExit(service, storage);
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Java Task Manager ===");
        System.out.println("1) Add task");
        System.out.println("2) List tasks");
        System.out.println("3) Update task status");
        System.out.println("4) Remove task");
        System.out.println("5) Save and exit");
        System.out.print("Enter choice: ");
    }

    private static void addTask(Scanner scanner, TaskService service) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        LocalDate dueDate;
        while (true) {
            System.out.print("Due date (YYYY-MM-DD): ");
            String dueDateInput = scanner.nextLine().trim();
            try {
                dueDate = LocalDate.parse(dueDateInput);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        Task task = service.addTask(title, description, dueDate);
        System.out.println("Added task: " + task);
    }

    private static void listTasks(TaskService service) {
        List<Task> tasks = service.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\nTasks:");
        tasks.forEach(System.out::println);
    }

    private static void updateTaskStatus(Scanner scanner, TaskService service) {
        int taskId = readTaskId(scanner);

        System.out.println("Select status: 1) TODO  2) IN_PROGRESS  3) DONE");
        String statusInput = scanner.nextLine().trim();

        TaskStatus status = switch (statusInput) {
            case "1" -> TaskStatus.TODO;
            case "2" -> TaskStatus.IN_PROGRESS;
            case "3" -> TaskStatus.DONE;
            default -> null;
        };

        if (status == null) {
            System.out.println("Invalid status choice.");
            return;
        }

        if (service.updateStatus(taskId, status)) {
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void removeTask(Scanner scanner, TaskService service) {
        int taskId = readTaskId(scanner);
        if (service.removeTask(taskId)) {
            System.out.println("Task removed.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static int readTaskId(Scanner scanner) {
        while (true) {
            System.out.print("Task ID: ");
            String idInput = scanner.nextLine().trim();
            try {
                return Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric task ID.");
            }
        }
    }

    private static void saveAndExit(TaskService service, TaskStorage storage) {
        try {
            storage.save(STORAGE_PATH, service.getAllTasks());
            System.out.println("Tasks saved. Goodbye!");
        } catch (IOException e) {
            System.out.println("Failed to save tasks: " + e.getMessage());
        }
    }
}
