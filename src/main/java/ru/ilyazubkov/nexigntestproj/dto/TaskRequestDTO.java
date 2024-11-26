package ru.ilyazubkov.nexigntestproj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TaskRequestDTO {
    @NotBlank(message = "Task name required")
    private String name;

    @Positive(message = "Duration must be positive")
    private Long duration;
}
