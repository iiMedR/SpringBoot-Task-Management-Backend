package org.example.taskflow.service;

import org.example.taskflow.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1L;

    public Task createTask(Task task) {
        task.setId(nextId);
        task.setCompleted(false);

        nextId++;

        tasks.add(task);

        return task;
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public Task getTaskById(Long id) {
        for (Task task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                return task;
            }
        }

        return null;
    }

    public boolean deleteTaskById(Long id) {
        return tasks.removeIf(task ->
                Objects.equals(task.getId(), id)
        );
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);

        if (existingTask == null) {
            return null;
        }
        if (updatedTask.getTitle() == null || updatedTask.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return existingTask;
    }

    public Task markCompleted(Long id) {
        Task task = getTaskById(id);

        if(task == null) {
            return null;
        }

        task.setCompleted(true);

        return task;
    }
}