package com.v2p.swp391;

import com.v2p.swp391.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Swp391Application {

	public static void main(String[] args) {
		SpringApplication.run(Swp391Application.class, args);
	}

}
