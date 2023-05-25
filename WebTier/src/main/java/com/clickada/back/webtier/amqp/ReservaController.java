package com.clickada.back.webtier.amqp;

import com.clickada.back.webtier.dtos.ReservaAutomaticaDto;
import com.clickada.back.webtier.dtos.ReservaDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class ReservaController {

    private final RabbitTemplate rabbitTemplate;

    public ReservaController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PutMapping("/reservasVivas")
        /* Temp - habrá que decidir si unicamente del día actual  o de todos los dias posteriores */
    ResponseEntity<String> obtenerReservasVivas(@RequestParam UUID idPersona) {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("reservasVivas"); //Operación
        datos.add(idPersona.toString());

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("reservas", datos);

        if (resp!=null && resp.equals("error")) {
            return new ResponseEntity<>("La persona no existe, o no es gerente",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/todasReservas")
    ResponseEntity<?> todasReservas() throws Exception {
        ArrayList<String> datos = new ArrayList<>();
        datos.add("todasReservas"); //Operación
        String res = (String) this.rabbitTemplate.convertSendAndReceive("reservas", datos);

        if (res !=null && res.contains("ERR:")) {
            return new ResponseEntity<>(res.substring(4),HttpStatus.BAD_REQUEST);
        }
        else if (res == null) {return new ResponseEntity<>("Error en la petición",HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PostMapping("/reservarEspacio")
    String reservar(@RequestBody ReservaDto reservaDto) throws TimeoutException {

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

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("reservas", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }

    @PostMapping("/reservarAutomatica")
    String reservarAutomatica(@RequestBody ReservaAutomaticaDto reserva) throws TimeoutException {

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

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("reservas", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }

    @DeleteMapping("/eliminarReserva")
    String reservarAutomatica(@RequestParam UUID idPersona, @RequestParam UUID idReserva) throws TimeoutException {

        ArrayList<String> datos = new ArrayList<>();
        datos.add("eliminarReserva"); //Operación
        datos.add(String.valueOf(idPersona));
        datos.add(String.valueOf(idReserva));

        String resp = (String) this.rabbitTemplate.convertSendAndReceive("reservas", datos);

        if (resp == null) {
            throw new TimeoutException();
        }
        return resp;
    }
}