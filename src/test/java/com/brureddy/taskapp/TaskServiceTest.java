package com.brureddy.taskapp;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Test
    void addAndFindTaskWorks() {
        TaskService service = new TaskService();

        Task task = service.addTask("Learn Java", "Practice OOP", LocalDate.of(2026, 1, 1));

        assertEquals(1, service.size());
        assertTrue(service.findById(task.getId()).isPresent());
    }

    @Test
    void updateStatusReturnsFalseForUnknownTask() {
        TaskService service = new TaskService();

        boolean updated = service.updateStatus(100, TaskStatus.DONE);

        assertFalse(updated);
    }

    @Test
    void removeTaskDeletesTask() {
        TaskService service = new TaskService();
        Task task = service.addTask("Build App", "CLI app", LocalDate.now());

        assertTrue(service.removeTask(task.getId()));
        assertEquals(0, service.size());
    }
}
