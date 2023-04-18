package com.clickada.back.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class EnviaMail {

    @Autowired
    private JavaMailSenderImpl emailSender = new JavaMailSenderImpl();

    @Async
    public void enviarCorreo(String dest,String texto) {
        emailSender.setHost("smtp.gmail.com");
        emailSender.setPort(587);
        emailSender.setUsername("clickada2023");
        emailSender.setPassword("cqemjixvgdmftpnf");
        Properties props = emailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("clickada2023@gmail.com");
        message.setTo(dest);
        message.setSubject("Reserva cancelada");
        message.setText("Hola, ha realizado una reserva que ha sido cancelada por cambios en la ocupación " +
                "del espacio");
        emailSender.send(message);
    }
}