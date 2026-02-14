# Java Task Manager App

A complete Java console application for tracking tasks with persistent local storage.

## Features
- Add tasks with title, description, and due date.
- View all tasks sorted by due date.
- Update task status (`TODO`, `IN_PROGRESS`, `DONE`).
- Remove tasks by ID.
- Save/load tasks from `tasks.db`.

## Tech
- Java 17
- Maven
- JUnit 5

## Run Locally
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.brureddy.taskapp.App"
```

## Run Tests
```bash
mvn test
```

## Project Structure
```text
src/main/java/com/brureddy/taskapp
  App.java
  Task.java
  TaskService.java
  TaskStatus.java
  TaskStorage.java
src/test/java/com/brureddy/taskapp
  TaskServiceTest.java
```
