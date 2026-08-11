package org.example.taskflow.service;

import org.example.taskflow.exception.TaskNotFoundException;
import org.example.taskflow.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1L;

    public Task createTask(String title, String description) {
        Task task = new Task();
        task.setId(nextId);
        task.setTitle(title);
        task.setDescription(description);
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
        throw new TaskNotFoundException(id);
    }

    public void deleteTaskById(Long id) {
        Task task = getTaskById(id);
        tasks.remove(task);
    }

    public Task updateTask(Long id, String title, String description, boolean completed) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);

        return existingTask;
    }

    public Task markCompleted(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(true);
        return task;
    }
}