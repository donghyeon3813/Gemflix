package com.movie.Gemflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //BaseEntity 사용하기 위함
public class GemflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(GemflixApplication.class, args);
	}

}
