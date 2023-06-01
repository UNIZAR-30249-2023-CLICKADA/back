package com.clickada.back.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

@Service
public class EnviaMail {

    @Autowired
    private JavaMailSenderImpl emailSender = new JavaMailSenderImpl();

    @Async
    public void enviarCorreo(String dest, int tipo, String nombre, LocalDate fecha, LocalTime hora) {
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
        if (tipo==1){
            message.setText("Hola, una de sus reservas ha sido cancelada por cambios en la ocupación " +
                    "del espacio. Su reserva del " + fecha.toString() + " a las " +
                    hora.toString() + " ha sido cancelada porque ha cambiado la ocupación máxima del espacio y " +
                    "su reserva ya no cumple las condiciones de uso.");
        }
        else if (tipo==2){
            message.setText("Hola, una de sus reservas ha sido cancelada." +
                    " Su reserva del " + fecha.toString() + " a las " +
                    hora.toString() + " ha sido cancelada porque un gerente ha eliminado la reserva.");
        }
        emailSender.send(message);
    }
}