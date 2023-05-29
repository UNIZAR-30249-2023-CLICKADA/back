package com.clickada.back.webtier.amqp;

import com.clickada.back.webtier.dtos.ReservaAutomaticaDto;
import com.clickada.back.webtier.dtos.ReservaDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
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


    @PutMapping("/cambiarPorcentajeUsoEspacio")
    ResponseEntity<?> cambiarPorcentajeEspacio(@RequestParam UUID idPersona, @RequestParam UUID idEspacio,
                             @RequestParam double porcentaje) throws TimeoutException {
        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarPorcentajeEspacio"); //Operación
        datos.add(idPersona.toString());
        datos.add(idEspacio.toString());
        datos.add(String.valueOf(porcentaje));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp !=null && resp.contains("ERR:")) {
            return new ResponseEntity<>(resp.substring(4),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PutMapping("/cambiarPorcentajeUsoEdificio")
    ResponseEntity<?> cambiarPorcentajeEdificio(@RequestParam UUID idPersona, @RequestParam double porcentaje) throws TimeoutException {
        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarPorcentajeEdificio"); //Operación
        datos.add(idPersona.toString());
        datos.add(String.valueOf(porcentaje));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp !=null && resp.contains("ERR:")) {
            return new ResponseEntity<>(resp.substring(4),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PutMapping("/cambiarReservabilidad")
    ResponseEntity<?> cambiarReservabilidad(@RequestParam UUID idPersona,  @RequestParam UUID idEspacio,
                                            @RequestParam boolean reservable, @RequestParam String categoriaReserva) throws Exception {
        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarReservabilidad"); //Operación
        datos.add(idPersona.toString());
        datos.add(idEspacio.toString());
        datos.add(String.valueOf(reservable));
        datos.add(categoriaReserva);

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp !=null && resp.contains("ERR:")) {
            return new ResponseEntity<>(resp.substring(4),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
}