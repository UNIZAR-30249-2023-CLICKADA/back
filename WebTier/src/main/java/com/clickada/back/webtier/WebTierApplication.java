package com.clickada.back.webtier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.*;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebTierApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTierApplication.class, args);
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
