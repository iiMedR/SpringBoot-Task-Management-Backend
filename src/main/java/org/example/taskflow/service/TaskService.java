package org.example.taskflow.service;

import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.exception.TaskNotFoundException;
import org.example.taskflow.model.Task;
import org.example.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    public TaskResponse createTask(String title, String description) {
        Task task = new Task();

        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskResponse getTaskResponseById(Long id){
        Task task = getTaskById(id);
        return toResponse(task);
    }

    public void deleteTaskById(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    public TaskResponse updateTask(Long id, String title, String description, boolean completed) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);

        Task savedTask = taskRepository.save(existingTask);

        return toResponse(savedTask);
    }

    public TaskResponse markCompleted(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(true);
        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }
}