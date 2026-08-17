package org.example.taskflow.repository;

import org.example.taskflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByCompleted(boolean completed, Pageable pageable);

    Page<Task> findByTitleContainingIgnoreCase(String search, Pageable pageable);

    Page<Task> findByCompletedAndTitleContainingIgnoreCase(boolean completed, String search, Pageable pageable);

    Page<Task> findByUserId(Long userId, Pageable pageable);
}
