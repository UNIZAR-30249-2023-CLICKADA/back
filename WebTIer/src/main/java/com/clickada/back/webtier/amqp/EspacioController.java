package com.clickada.back.webtier.amqp;

import com.clickada.back.webtier.dtos.ReservaDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        if (resp == null) {
            return "F";//  throw new TimeoutException();
        }
        return resp;

    }
}