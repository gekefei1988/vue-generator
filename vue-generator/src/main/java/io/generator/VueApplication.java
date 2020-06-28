package io.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.generator.dao")
public class VueApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueApplication.class, args);
	}
}
