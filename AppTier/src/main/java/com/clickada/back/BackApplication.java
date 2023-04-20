package com.clickada.back;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = {"com.clickada.back.*"})
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	//Quizás una cola de mensajes para cada controlador
	@Bean
	public Queue personaQueue() { return new Queue("personas"); }

	@Bean
	public Queue espacioQueue() { return new Queue("espacios"); }

	@Bean
	public Queue reservaQueue() { return new Queue("reservas"); }

	@Bean
	public Queue responseQueue() { return new Queue("respuestas"); }

}
