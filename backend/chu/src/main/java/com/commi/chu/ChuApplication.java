package com.commi.chu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChuApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChuApplication.class, args);
	}

}
