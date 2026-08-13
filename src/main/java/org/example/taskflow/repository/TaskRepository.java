package org.example.taskflow.repository;

import org.example.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String search);

    List<Task> findByCompletedAndTitleContainingIgnoreCase(boolean completed, String search);
}
