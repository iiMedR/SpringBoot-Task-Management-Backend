package org.example.taskflow.repository;

import org.example.taskflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //Page<Task> findByCompleted(boolean completed, Pageable pageable);
    Page<Task> findByUserIdAndCompleted(Long userId, boolean completed, Pageable pageable);

    //Page<Task> findByTitleContainingIgnoreCase(String search, Pageable pageable);
    Page<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String search, Pageable pageable);

    //Page<Task> findByCompletedAndTitleContainingIgnoreCase(boolean completed, String search, Pageable pageable);
    Page<Task> findByUserIdAndCompletedAndTitleContainingIgnoreCase(Long userId, boolean completed, String search, Pageable pageable);

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
