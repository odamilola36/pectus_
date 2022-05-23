package com.lomari.pectus_;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class PectusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PectusApplication.class, args);
	}

}
