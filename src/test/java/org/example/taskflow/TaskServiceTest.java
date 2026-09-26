package org.example.taskflow;

import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.exception.TaskNotFoundException;
import org.example.taskflow.model.Task;
import org.example.taskflow.model.User;
import org.example.taskflow.repository.TaskRepository;
import org.example.taskflow.repository.UserRepository;
import org.example.taskflow.service.TaskService;
import org.example.taskflow.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldReturnTaskWhenTaskBelongsToCurrentUser(){
        //Arrange

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        Task task = new Task();
        task.setId(10L);
        task.setTitle("test");
        task.setUser(user);

        when(userService.getCurrentUser())
                .thenReturn(user);

        when(taskRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(task));

        //Act

        Task result = taskService.getTaskById(10L);

        //Assert
        assertEquals(10L, result.getId());
        assertEquals("test", result.getTitle());

        verify(userService).getCurrentUser();
        verify(taskRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotBelongToCurrentUser(){

        //Arrange
        User user = new User();
        user.setId(1L);

        when(userService.getCurrentUser())
                .thenReturn(user);

        when(taskRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.empty());

        //Act + Assert

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(10L)
        );
    }

    @Test
    void shouldCreateTaskForCurrentUser(){

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle("Learn Mockito");
        savedTask.setDescription("Practice unit Test");
        savedTask.setCompleted(false);
        savedTask.setUser(user);

        when(userService.getCurrentUser())
                .thenReturn(user);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        //Act
        TaskResponse result = taskService.createTask(
                "Learn Mockito",
                "Practice unit Test"
        );

        //Assert
        assertEquals(10L, result.getId());
        assertEquals("Learn Mockito", result.getTitle());
        assertFalse(result.isCompleted());
        assertEquals(1L, result.getUserId());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();

        assertEquals("Learn Mockito", capturedTask.getTitle());
        assertEquals("Practice unit Test", capturedTask.getDescription());
        assertFalse(capturedTask.isCompleted());
        assertEquals(1L, capturedTask.getUser().getId());
    }
}
