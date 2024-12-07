package com.accenture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.accenture"})
public class AccentureChallengeJavaApplication implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(AccentureChallengeJavaApplication.class,args);
	}


	public AccentureChallengeJavaApplication(@Value("${app.version}") String appVersion,
											 @Value("${app.name}")String appName) {
		log.info("*** {} ***",appName);
		log.info("*** version: {} ***", appVersion);
	}
}
