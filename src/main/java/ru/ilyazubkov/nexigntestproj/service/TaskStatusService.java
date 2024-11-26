package ru.ilyazubkov.nexigntestproj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ilyazubkov.nexigntestproj.exception.TaskNotFoundException;
import ru.ilyazubkov.nexigntestproj.model.Task;
import ru.ilyazubkov.nexigntestproj.model.TaskStatus;
import ru.ilyazubkov.nexigntestproj.repository.TaskRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskRepository taskRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTaskStatus(Long taskId, TaskStatus newStatus, String result) throws TaskNotFoundException {

        Task task= taskRepository.findById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException(String.valueOf(taskId)));

        log.debug("Updating task {} to status {}. Current version: {}",
                taskId, newStatus, task.getVersion());

        // Обновляем статус и результат
        task.setStatus(newStatus);
        task.setResult(result);
        // Сохраняем изменения в БД
        taskRepository.save(task);
    }
}
