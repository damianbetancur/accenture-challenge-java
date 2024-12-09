package com.accenture.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class ThreadPoolMetrics {

    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolMetrics(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Scheduled(fixedRate = 5000) // Cada 5 segundos
    public void logThreadPoolStats() {
        log.info("Active Threads: {}", threadPoolExecutor.getActiveCount());
        log.info("Completed Tasks: {}", threadPoolExecutor.getCompletedTaskCount());
        log.info("Task Queue Size: {}", threadPoolExecutor.getQueue().size());
    }
}

