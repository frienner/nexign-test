package ru.ilyazubkov.nexigntestproj.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class WorkerConfig {

    private final int poolSize;

    public WorkerConfig() {
        poolSize = Runtime.getRuntime().availableProcessors() + 1;
    }

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(poolSize);
    }
}
