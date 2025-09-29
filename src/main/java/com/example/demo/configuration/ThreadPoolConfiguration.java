package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfiguration {

	@Bean("Thread")
	ThreadPoolTaskExecutor configThreadPool() {
		var executor=new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setQueueCapacity(1);
		executor.setMaxPoolSize(5);
		executor.initialize();
		return executor;
	}
}
