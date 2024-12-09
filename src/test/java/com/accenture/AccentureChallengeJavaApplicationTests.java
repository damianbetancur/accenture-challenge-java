package com.accenture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccentureChallengeJavaApplicationTest {

	@Value("${app.version}")
	private String appVersion;

	@Value("${app.name}")
	private String appName;

	@Test
	void contextLoads() {
		assertThat(appVersion).isNotNull();
		assertThat(appName).isNotNull();

		assertThat(appVersion).isEqualTo("1.0.0");
		assertThat(appName).isEqualTo("accenture-challenge-java");
	}
}
