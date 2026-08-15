package org.example.taskflow.controller;

import jakarta.validation.Valid;
import org.example.taskflow.dto.CreateTaskRequest;
import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.dto.UpdateTaskRequest;
import org.example.taskflow.model.Task;
import org.example.taskflow.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request.getTitle(),  request.getDescription());
    }

    @GetMapping
    public Page<TaskResponse> getAllTasks(@RequestParam(required = false) Boolean completed, @RequestParam(required = false) String search, Pageable pageable) { return taskService.getAllTasks(completed, search, pageable); }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {

        return ResponseEntity.ok(taskService.getTaskResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,@Valid @RequestBody UpdateTaskRequest request) {
        TaskResponse taskUpdated = taskService.updateTask(
                id,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );
        return ResponseEntity.ok(taskUpdated);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {

        return ResponseEntity.ok(taskService.markCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/user/{userId}")
    public Task assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return taskService.assignTaskToUser(taskId, userId);
    }
}
