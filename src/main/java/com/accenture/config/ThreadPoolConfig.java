package com.accenture.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${executor.corePoolSize:50}")
    private int corePoolSize;

    @Value("${executor.maxPoolSize:500}")
    private int maxPoolSize;

    @Value("${executor.keepAliveTime:60}")
    private long keepAliveTime;

    @Value("${executor.queueCapacity:500}")
    private int queueCapacity;

    @Bean(name = "taskExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize, // Núcleo inicial de hilos
                maxPoolSize, // Máximo de hilos permitidos
                keepAliveTime, // Tiempo de vida de hilos inactivos
                TimeUnit.SECONDS, // unidad de medida
                new LinkedBlockingQueue<>(queueCapacity) // Asignar una cola válida con capacidad
        );
    }
}
