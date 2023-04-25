package com.clickada.back.infrastructure;

import com.clickada.back.application.DominioService;
import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import com.clickada.back.dtos.ReservaAutomaticaDto;
import com.clickada.back.dtos.ReservaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class EspacioController {
    EspacioService espacioService;
    ReservaService reservaService;
    PersonaService personaService;
    DominioService dominioService;

    @Autowired
    public EspacioController(EspacioService espacioService,ReservaService reservaService,PersonaService personaService,
                             DominioService dominioService) {
        this.espacioService = espacioService;
        this.reservaService = reservaService;
        this.personaService = personaService;
        this.dominioService = dominioService;
    }
    @GetMapping("/todosEspacios")
    ResponseEntity<?> todosEspacios(){
        return new ResponseEntity<>(espacioService.todosEspacios(), HttpStatus.OK);
    }

    @GetMapping("/todasReservas")
    ResponseEntity<?> todasReservas() throws Exception {
        return new ResponseEntity<>(reservaService.listarTodasReservas() ,HttpStatus.OK);
    }
    @PostMapping("/reservarEspacio")
    ResponseEntity<?> reservar(@RequestBody ReservaDto reservaDto) throws Exception {
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        //convert String to LocalDate
        LocalDate fecha = LocalDate.parse(reservaDto.getFecha(), formatterDay);
        LocalTime horaInicio = LocalTime.parse(reservaDto.getHoraInicio(), formatterTime);
        LocalTime horaFinal = LocalTime.parse(reservaDto.getHoraFinal(), formatterTime);
        TipoUso tipoUso = TipoUso.getTipoUsoByString(reservaDto.getStringTipoUso());
        if(tipoUso==null) {
            return new ResponseEntity<>("Ese tipo de uso no existe, pruebe con otro",HttpStatus.BAD_REQUEST);
        }
        try {
            dominioService.reservarEspacio(reservaDto.getIdPersona(), reservaDto.getIdEspacios(),
                    fecha,horaInicio,horaFinal,
                    tipoUso, reservaDto.getNumMaxPersonas(), reservaDto.getDetalles());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Reserva realizada correctamente", HttpStatus.OK);
    }
    @PostMapping("/reservarAutomatica")
    ResponseEntity<?> reservarAutomatica(@RequestBody ReservaAutomaticaDto reservaAutomaticaDto) throws Exception {
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        //convert String to LocalDate
        LocalDate fecha = LocalDate.parse(reservaAutomaticaDto.getFecha(), formatterDay);
        LocalTime horaInicio = LocalTime.parse(reservaAutomaticaDto.getHoraInicio(), formatterTime);
        LocalTime horaFinal = LocalTime.parse(reservaAutomaticaDto.getHoraFinal(), formatterTime);
        try {
            List<UUID> listEspacios = espacioService.buscarEspacios(reservaAutomaticaDto.getIdPersona(), reservaAutomaticaDto.getNumEspacios(),
                    fecha,horaInicio,horaFinal, reservaAutomaticaDto.getNumMaxPersonas(),reservaAutomaticaDto.getTipoUso(), reservaAutomaticaDto.getDetalles());

            return new ResponseEntity<>(listEspacios, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(){
        espacioService.eliminarTodos();
        reservaService.eliminarTodas();
        return new ResponseEntity<>("Eliminados datos de las bases de datos", HttpStatus.OK);
    }

    @PutMapping("/cambiarPorcentajeUsoEspacio")
    ResponseEntity<?> reservarEspacio(@RequestParam UUID idPersona, @RequestParam UUID idEspacio, @RequestParam double porcentaje){
        try{
            espacioService.cambiarPorcentajeEspacio(idPersona,idEspacio,porcentaje);
            return new ResponseEntity<>("Porcentaje cambiado correctamente", HttpStatus.OK);

        }catch(Exception e){
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/cambiarPorcentajeUsoEdificio")
    ResponseEntity<?> reservarEdificio(@RequestParam UUID idPersona, @RequestParam double porcentaje){
        try{
            espacioService.cambiarPorcentajeEdificio(idPersona,porcentaje);

            return new ResponseEntity<>("Porcentaje cambiado correctamente", HttpStatus.OK);
        }catch(Exception e){
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
