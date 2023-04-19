package com.clickada.back.webtier.amqp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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

        String response = (String) this.rabbitTemplate.convertSendAndReceive("persona",idPersona.toString()
        +rol);

        if (response == null) {
            throw new TimeoutException();
        }

        // Lo que conseguimos es que efectivamente este método que responde a una
        // petición GET enviando un mensaje y esperando su respuesta parezca un
        // método síncrono, normal(con la diferencia de que hay un timeout y saltará una
        // excepción si la respuesta no llega en X segundos).
        return String.format("Tu resultado es %d!", response);
    }
/*
    @GetMapping("/todasPersonas")
    ResponseEntity<?> todasPersonas(){
        //return new ResponseEntity<>(personaService.todasPersonas(),HttpStatus.OK);
    }


    @PutMapping("/cambiarReservabilidad")
    ResponseEntity<?> cambiarReservabilidad(@RequestParam UUID idPersona,  @RequestParam UUID idEspacio,
                                            @RequestParam boolean reservable, @RequestParam String categoriaReserva) throws Exception {
        /*if (personaService.aptoParaCambiar(idPersona)){
            try{
                if(espacioService.cambiarReservabilidadEspacio(idEspacio,new Reservabilidad(reservable,categoriaReserva),idPersona)){
                    return new ResponseEntity<>("Reservabilidad cambiada correctamente", HttpStatus.OK);
                }
                return new ResponseEntity<>("No existe el espacio para cambiar la reservabilidad",HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>("La persona no es apta para cambiar nada",HttpStatus.BAD_REQUEST);*/

}

