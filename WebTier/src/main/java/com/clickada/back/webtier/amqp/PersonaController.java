package com.clickada.back.webtier.amqp;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class PersonaController {

    private final RabbitTemplate rabbitTemplate;

    public PersonaController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @PutMapping("/cambiarRol")
    String cambiarRolPersona(@RequestParam UUID idPersona, @RequestParam String rol) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarRol"); //Operación
        datos.add(idPersona.toString());
        datos.add(rol);
        String response = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (response == null) {
            throw new TimeoutException();
        }
        return response;
    }

    @PutMapping("/cambiarDepartamento")
    String cambiarDepartamentoPersona(@RequestParam UUID idGerente,@RequestParam UUID idPersona, @RequestParam String nuevoDpto) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarDpto"); //Operación
        datos.add(idGerente.toString());
        datos.add(idPersona.toString());
        datos.add(nuevoDpto);
        String response = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (response == null) {
            throw new TimeoutException();
        }
        return response;
    }

    @PutMapping("/agregarPersona")
    String agregarPersona(@RequestParam UUID idGerente,@RequestParam UUID idPersona, @RequestParam String nuevoDpto) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarDpto"); //Operación
        datos.add(idGerente.toString());
        datos.add(idPersona.toString());
        datos.add(nuevoDpto);
        String response = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (response == null) {
            throw new TimeoutException();
        }
        return response;
    }


    @GetMapping("/todasPersonas")
    ResponseEntity<String> todasPersonas() throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("todasPersonas"); //Operación
        String resp = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @PutMapping("/loginPersona")
    ResponseEntity<String> loginPersona(@RequestParam String email, @RequestParam String pass) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("loginPersona"); //Operación
        datos.add(email);
        datos.add(pass);
        String resp = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (resp == null) {throw new TimeoutException();}
        else if (resp.equals("Login fallido")) {return new ResponseEntity<>("Login Fallido", HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PutMapping("/permisosReserva")
    String permisosDeReserva(@RequestParam UUID id) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("permisosReserva"); //Operación
        datos.add(id.toString());
        String resp = (String) this.rabbitTemplate.convertSendAndReceive("personas", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }


    // Si salta una excepción de Timeout en cualquier petición de este Controller devolvemos
    // un código de error y un mensaje explicándolo
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    public String timeout() {
        return "No se puede atender la petición en este momento, inténtalo de nuevo más tarde.";
    }



    // Lo que conseguimos es que efectivamente este método que responde a una
    // petición GET enviando un mensaje y esperando su respuesta parezca un
    // método síncrono, normal(con la diferencia de que hay un timeout y saltará una
    // excepción si la respuesta no llega en X segundos).

}