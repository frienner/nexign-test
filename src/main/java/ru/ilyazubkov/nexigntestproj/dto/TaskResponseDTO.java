package ru.ilyazubkov.nexigntestproj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ilyazubkov.nexigntestproj.model.Task;
import ru.ilyazubkov.nexigntestproj.model.TaskStatus;

@Data
@AllArgsConstructor
public class TaskResponseDTO {

    private String name;

    private String result;

    private TaskStatus status;

    public static TaskResponseDTO convertTaskToResponseDTO(Task task) {
        return new TaskResponseDTO(task.getName(), task.getResult(), task.getStatus());
    }
}
