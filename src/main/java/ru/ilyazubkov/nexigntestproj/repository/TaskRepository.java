package ru.ilyazubkov.nexigntestproj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ilyazubkov.nexigntestproj.model.Task;
import ru.ilyazubkov.nexigntestproj.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findFirstByStatus(TaskStatus status);

    List<Task> findAllByStatus(TaskStatus status);
}
