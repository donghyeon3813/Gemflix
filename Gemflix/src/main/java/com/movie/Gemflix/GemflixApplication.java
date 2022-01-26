package com.movie.Gemflix;

import com.movie.Gemflix.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = {ApiProperties.class})
public class GemflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(GemflixApplication.class, args);
	}

}
