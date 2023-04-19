package com.clickada.back.webtier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.*;



@SpringBootApplication
public class WebTierApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTierApplication.class, args);
    }

    @Bean
    public Queue personaQueue() { return new Queue("personas"); }
    @Bean
    public Queue duplicarQueue() { return new Queue("duplicar"); }
    @Bean
    public Queue responseQueue() { return new Queue("response"); }
}
