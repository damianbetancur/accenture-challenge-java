package com.accenture.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ThreadPoolMetrics {

    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolMetrics(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Scheduled(fixedRate = 5000) // Cada 5 segundos
    public void logThreadPoolStats() {
        System.out.println("Active Threads: " + threadPoolExecutor.getActiveCount());
        System.out.println("Completed Tasks: " + threadPoolExecutor.getCompletedTaskCount());
        System.out.println("Task Queue Size: " + threadPoolExecutor.getQueue().size());
    }
}

