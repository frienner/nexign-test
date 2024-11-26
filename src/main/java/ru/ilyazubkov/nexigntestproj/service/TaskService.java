package ru.ilyazubkov.nexigntestproj.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ilyazubkov.nexigntestproj.event.TaskCreatedEvent;
import ru.ilyazubkov.nexigntestproj.exception.TaskNotFoundException;
import ru.ilyazubkov.nexigntestproj.model.Task;
import ru.ilyazubkov.nexigntestproj.model.TaskStatus;
import ru.ilyazubkov.nexigntestproj.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService{

    private final TaskRepository taskRepository;

    private final TaskExecutionService taskRetrievalService;

    @Transactional
    public void saveTask(TaskCreatedEvent taskCreatedEvent) {
        Task task = Task.builder()
                .name(taskCreatedEvent.getName())
                .duration(taskCreatedEvent.getDuration())
                .status(TaskStatus.CREATED)
                .build();

        taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) { return taskRepository.findById(id); }

    @Scheduled(fixedDelay = 2000)
    public void processTask() {
        log.info("Looking for tasks");
        taskRepository.findAllByStatus(TaskStatus.CREATED)
                .forEach(task -> {
                    try {
                        taskRetrievalService.retrieveTaskWithRetry(task);
                    } catch (TaskNotFoundException e) {
                        log.error("Task not found: {}", e.getMessage(), e);
                    }
                });
    }
}
