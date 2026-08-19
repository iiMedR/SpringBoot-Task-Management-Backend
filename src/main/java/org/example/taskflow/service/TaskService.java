package org.example.taskflow.service;

import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.exception.TaskNotFoundException;
import org.example.taskflow.exception.UserNotFoundException;
import org.example.taskflow.model.Task;
import org.example.taskflow.model.User;
import org.example.taskflow.repository.TaskRepository;
import org.example.taskflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private TaskResponse toResponse(Task task) {
        Long userId = task.getUser() != null
                ? task.getUser().getId()
                : null;
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted(), userId);
    }

    public TaskResponse createTask(String title, String description) {
        Task task = new Task();

        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }

    public Page<TaskResponse> getAllTasks(Boolean completed, String search, Pageable pageable) {
        Page<Task> tasks;
        boolean hasSearch = search != null && !search.isBlank();

        if(completed != null && hasSearch) {
            tasks = taskRepository.findByCompletedAndTitleContainingIgnoreCase(completed, search, pageable);
        }
        else if(completed != null) {
            tasks = taskRepository.findByCompleted(completed, pageable);
        }
        else if(hasSearch) {
            tasks = taskRepository.findByTitleContainingIgnoreCase(search, pageable);
        }
        else {
            tasks = taskRepository.findAll(pageable);
        }

        return tasks.map(this::toResponse);
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

    @Transactional
    public TaskResponse updateTask(Long id, String title, String description, boolean completed) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);

        //Task savedTask = taskRepository.save(existingTask);

        return toResponse(existingTask);
    }

    public TaskResponse markCompleted(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(true);
        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }

    public TaskResponse assignTaskToUser(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        task.setUser(user);
        taskRepository.save(task);
        return toResponse(task);
    }

    public Page<TaskResponse> getTasksByUserId(Long userId, Pageable pageable){
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Page<Task> tasks = taskRepository.findByUserId(userId, pageable);
        return tasks.map(this::toResponse);
    }
}