package com.clickada.back.webtier.amqp;

import com.clickada.back.webtier.dtos.ReservaAutomaticaDto;
import com.clickada.back.webtier.dtos.ReservaDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class EspacioController {

    private final RabbitTemplate rabbitTemplate;

    public EspacioController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/todosEspacios")
    String todosEspacios() throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("todosEspacios"); //Operación
        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }


    @PutMapping("/cambiarPorcentajeUso")
    String cambiarPorcentaje(@RequestParam UUID idPersona, @RequestParam UUID idEspacio,
                             @RequestParam int porcentaje) throws TimeoutException {
        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarPorcentajeUso"); //Operación
        datos.add(idPersona.toString());
        datos.add(idEspacio.toString());
        datos.add(String.valueOf(porcentaje));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }
}