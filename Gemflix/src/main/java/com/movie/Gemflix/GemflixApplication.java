package com.movie.Gemflix;

import com.movie.Gemflix.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = {ApiProperties.class})
@EnableJpaAuditing //BaseEntity 사용하기 위함
public class GemflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(GemflixApplication.class, args);
	}

}
