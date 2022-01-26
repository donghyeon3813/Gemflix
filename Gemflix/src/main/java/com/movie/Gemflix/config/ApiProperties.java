package com.movie.Gemflix.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



@ConfigurationProperties(prefix = "api")
@Data
@ConstructorBinding
@AllArgsConstructor
public class ApiProperties {
    private final String key;
}
