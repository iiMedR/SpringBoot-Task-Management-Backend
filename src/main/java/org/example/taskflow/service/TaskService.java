package org.example.taskflow.service;

import org.example.taskflow.dto.TaskResponse;
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


    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }


    public TaskResponse createTask(String title, String description) {
        Task task = new Task();
        task.setId(nextId);
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);

        nextId++;

        tasks.add(task);

        return toResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return tasks.stream().map(this::toResponse).toList();
    }

    public Task getTaskById(Long id) {
        for (Task task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                return task;
            }
        }
        throw new TaskNotFoundException(id);
    }

    public TaskResponse getTaskResponseById(Long id){
        Task task = getTaskById(id);
        return toResponse(task);
    }

    public void deleteTaskById(Long id) {
        Task task = getTaskById(id);
        tasks.remove(task);
    }

    public TaskResponse updateTask(Long id, String title, String description, boolean completed) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);

        return toResponse(existingTask);
    }

    public TaskResponse markCompleted(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(true);
        return toResponse(task);
    }
}