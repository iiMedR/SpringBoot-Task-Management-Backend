package org.example.taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.taskflow.dto.CreateTaskRequest;
import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.dto.UpdateTaskRequest;
import org.example.taskflow.model.User;
import org.example.taskflow.service.TaskService;
import org.example.taskflow.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @Operation(
            summary = "Create a task",
            description = "Create a task for the currently authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task Created"
            )
})
    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request.getTitle(),  request.getDescription());
    }

    @GetMapping
    public Page<TaskResponse> getAllTasks(@RequestParam(required = false) Boolean completed, @RequestParam(required = false) String search, Pageable pageable) { return taskService.getAllTasks(completed, search, pageable); }

    @Operation(
            summary = "Get task by id",
            description = "Get task belonging to the user by id "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
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

    /*@PatchMapping("/{taskId}/user/{userId}")
    public ResponseEntity<TaskResponse> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTaskToUser(taskId, userId));
    }*/

    @GetMapping("/testt")
    public List<TaskResponse> getAllTasksByEmail() {
        User currentUser = userService.getCurrentUser();
        return taskService.getTasksByUser(currentUser.getEmail());
    }
}
