package org.example.taskflow.repository;

import org.example.taskflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //Page<Task> findByCompleted(boolean completed, Pageable pageable);
    //Page<Task> findByUserIdAndCompleted(Long userId, boolean completed, Pageable pageable);

    //Page<Task> findByTitleContainingIgnoreCase(String search, Pageable pageable);
    //Page<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String search, Pageable pageable);

    //Page<Task> findByCompletedAndTitleContainingIgnoreCase(boolean completed, String search, Pageable pageable);
    //Page<Task> findByUserIdAndCompletedAndTitleContainingIgnoreCase(Long userId, boolean completed, String search, Pageable pageable);

    @Query("""
        select t
        from Task t
        where t.user.id = :userId
        and (:completed is NULL or t.completed = :completed)
        and (:search is NULL or LOWER(t.title) like LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Task> searchTasks(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed,
            @Param("search") String search,
            Pageable pageable
    );

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
