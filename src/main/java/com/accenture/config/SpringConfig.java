package com.accenture.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("com.accenture")
@Configuration
@EnableAutoConfiguration
public class SpringConfig implements WebMvcConfigurer {
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
}
