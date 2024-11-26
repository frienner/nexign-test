package ru.ilyazubkov.nexigntestproj.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import ru.ilyazubkov.nexigntestproj.exception.TaskNotFoundException;
import ru.ilyazubkov.nexigntestproj.model.Task;
import ru.ilyazubkov.nexigntestproj.model.TaskStatus;
import ru.ilyazubkov.nexigntestproj.repository.TaskRepository;

import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskExecutionService {

    private final TaskRepository taskRepository;
    private final ExecutorService executorService;

    private final TaskStatusService taskStatusService;

    @Transactional
    @Retryable(
            value = OptimisticLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public void retrieveTaskWithRetry(Task task) throws TaskNotFoundException {

        log.info("Task found: {}", task.getName());

        Task currentTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new TaskNotFoundException(String.valueOf(task.getId())));

        if (currentTask.getStatus() != TaskStatus.CREATED) {
            log.warn("Task {} already in processing", task.getId());
            return;
        }

        currentTask.setStatus(TaskStatus.IN_PROGRESS);
        Task savedTask = taskRepository.save(currentTask);

        executorService.submit(() ->
        {
            try {
                executeTask(savedTask);
            } catch (TaskNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void executeTask(Task task) throws TaskNotFoundException {
        try {
            Thread.sleep(task.getDuration());
            if (Math.random() > 0.1) {
                taskStatusService.updateTaskStatus(
                        task.getId(),
                        TaskStatus.COMPLETED,
                        "Task completed successfully"
                );
            } else
                throw new Exception("Unlucky");
        } catch (Exception e) {
            taskStatusService.updateTaskStatus(
                    task.getId(),
                    TaskStatus.FAILED,
                    "Error: " + e.getMessage()
            );
        }
    }

}
