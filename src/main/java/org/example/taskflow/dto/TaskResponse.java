package org.example.taskflow.dto;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Long userId;

    public TaskResponse(
            Long id,
            String title,
            String description,
            boolean completed,
            Long userId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Long getUserId() {
        return userId;
    }
}
