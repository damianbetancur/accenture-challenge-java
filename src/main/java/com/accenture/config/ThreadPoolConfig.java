package com.accenture.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${executor.corePoolSize:10}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize:100}")
    private int maxPoolSize;

    @Value("${executor.keepAliveTime:60}")
    private long keepAliveTime;

    @Value("${executor.queueCapacity:100}")
    private int queueCapacity;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity) // Asignar una cola válida con capacidad
        );
    }
}
