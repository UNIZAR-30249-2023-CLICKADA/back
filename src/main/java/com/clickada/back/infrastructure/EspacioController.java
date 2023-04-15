package com.clickada.back.infrastructure;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import com.clickada.back.dtos.ReservaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class EspacioController {
    EspacioService espacioService;
    ReservaService reservaService;

    @Autowired
    public EspacioController(EspacioService espacioService,ReservaService reservaService) {
        this.espacioService = espacioService;
        this.reservaService = reservaService;
    }
    @GetMapping("/todosEspacios")
    ResponseEntity<?> todosEspacios(){
        return new ResponseEntity<>(espacioService.todosEspacios(), HttpStatus.OK);
    }

    @PutMapping("/reservasVivas")
    /* Temp - habrá que decidir si unicamente del día actual  o de todos los dias posteriores */
    ResponseEntity<?> obtenerReservasVivas(@RequestParam UUID idPersona){
        return new ResponseEntity<>(espacioService.obtenerReservasVivas(idPersona), HttpStatus.OK);
    }
    @GetMapping("/todasReservas")
    ResponseEntity<?> todasReservas() throws Exception {
        return new ResponseEntity<>(reservaService.listarTodasReservas() ,HttpStatus.OK);
    }
    @PostMapping("/reservarEspacio")
    ResponseEntity<?> reservar(@RequestParam ReservaDto reservaDto) throws Exception {
        TipoUso tipoUso = TipoUso.getTipoUsoByString(reservaDto.getStringTipoUso());
        if(tipoUso==null) {
            return new ResponseEntity<>("Ese tipo de uso no existe, pruebe con otro",HttpStatus.BAD_REQUEST);
        }
        try {
            espacioService.reservarEspacio(reservaDto.getIdPersona(), reservaDto.getIdEspacios(),
                    reservaDto.getFecha(), reservaDto.getHoraInicio(), reservaDto.getHoraFinal(),
                    tipoUso, reservaDto.getNumMaxPersonas(), reservaDto.getDetalles());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Reserva realizada correctamente", HttpStatus.OK);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(){
        espacioService.eliminarTodos();
        reservaService.eliminarTodas();
        return new ResponseEntity<>("Eliminados datos de las bases de datos", HttpStatus.OK);
    }

}
