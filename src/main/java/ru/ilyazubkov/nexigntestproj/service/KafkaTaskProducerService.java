package ru.ilyazubkov.nexigntestproj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.ilyazubkov.nexigntestproj.dto.TaskRequestDTO;
import ru.ilyazubkov.nexigntestproj.event.TaskCreatedEvent;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaTaskProducerService {

    private final String topicName;

    private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

    public KafkaTaskProducerService(@Value("${spring.kafka.topic.name}") String topicName, KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createTask(TaskRequestDTO createTaskDto) {

        TaskCreatedEvent taskCreatedEvent = new TaskCreatedEvent(createTaskDto.getName(),
                createTaskDto.getDuration());

        CompletableFuture<SendResult<String, TaskCreatedEvent>> future = kafkaTemplate
                .send(topicName, taskCreatedEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

    }
}
