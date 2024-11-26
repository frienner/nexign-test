package ru.ilyazubkov.nexigntestproj.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ilyazubkov.nexigntestproj.event.TaskCreatedEvent;
import ru.ilyazubkov.nexigntestproj.service.TaskService;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "${spring.kafka.topic.name}")
public class TaskCreatedEventHandler {

    private final TaskService taskService;

    @KafkaHandler
    public void handle(TaskCreatedEvent taskCreatedEvent) {
        log.info("Received TaskCreatedEvent: {}", taskCreatedEvent);
        taskService.saveTask(taskCreatedEvent);

    }
}