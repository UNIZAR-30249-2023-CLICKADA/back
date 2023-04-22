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
            return "F";//  throw new TimeoutException();
        }
        return resp;

    }

    @PostMapping("/reservarEspacio")
    String reservar(@RequestBody ReservaDto reservaDto) throws Exception {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("reservarEspacio"); //Operación
        datos.add(reservaDto.getIdPersona().toString());
        datos.add(reservaDto.getIdEspacios().toString());
        datos.add(reservaDto.getStringTipoUso());
        datos.add(reservaDto.getFecha());
        datos.add(reservaDto.getHoraInicio());
        datos.add(reservaDto.getHoraFinal());
        datos.add(reservaDto.getDetalles());
        datos.add(String.valueOf(reservaDto.getNumMaxPersonas()));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if(resp == null) {
            return "F";//  throw new TimeoutException();
        }
        return resp;

    }

    @PostMapping("/reservarAutomatica")
    String reservarAutomatica(@RequestBody ReservaAutomaticaDto reserva) throws Exception {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("reservarAutomatica"); //Operación
        datos.add(reserva.getIdPersona().toString());
        datos.add(String.valueOf(reserva.getNumEspacios()));
        datos.add(String.valueOf(reserva.getNumMaxPersonas()));
        datos.add(reserva.getFecha());
        datos.add(reserva.getHoraInicio());
        datos.add(reserva.getHoraFinal());
        datos.add(reserva.getDetalles());
        datos.add(reserva.getTipoUso());

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp == null) {
            return "F";//  throw new TimeoutException();
        }
        return resp;
    }

    @PutMapping("/cambiarPorcentajeUso")
    String cambiarPorcentaje(@RequestParam UUID idPersona, @RequestParam UUID idEspacio, @RequestParam int porcentaje){
        ArrayList<String> datos = new ArrayList<>();
        datos.add("cambiarPorcentajeUso"); //Operación
        datos.add(idPersona.toString());
        datos.add(idEspacio.toString());
        datos.add(String.valueOf(porcentaje));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("espacios", datos);

        if (resp == null) {
            return "F";//  throw new TimeoutException();
        }
        return resp;
    }
}