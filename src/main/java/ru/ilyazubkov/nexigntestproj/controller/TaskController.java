package ru.ilyazubkov.nexigntestproj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ilyazubkov.nexigntestproj.dto.TaskRequestDTO;
import ru.ilyazubkov.nexigntestproj.dto.TaskResponseDTO;
import ru.ilyazubkov.nexigntestproj.exception.TaskNotFoundException;
import ru.ilyazubkov.nexigntestproj.service.KafkaTaskProducerService;
import ru.ilyazubkov.nexigntestproj.service.TaskService;

import java.util.List;

import static ru.ilyazubkov.nexigntestproj.dto.TaskResponseDTO.convertTaskToResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final KafkaTaskProducerService kafkaTaskProducerService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(convertTaskToResponseDTO(task)))
                .orElseThrow(() -> new TaskNotFoundException(String.valueOf(id)));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks().stream()
                .map(TaskResponseDTO::convertTaskToResponseDTO)
                .toList());
    }

    @PostMapping
    public ResponseEntity<String> createTask(@Validated  @RequestBody TaskRequestDTO taskRequestDTO) {
        kafkaTaskProducerService.createTask(taskRequestDTO);

        return ResponseEntity.ok("Task created: " + taskRequestDTO);
    }


}
