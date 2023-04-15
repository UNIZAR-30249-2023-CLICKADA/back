package com.clickada.back.infrastructure;

import com.clickada.back.application.EspacioService;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class EspacioController {
    EspacioService espacioService;

    @Autowired
    public EspacioController(EspacioService espacioService) {
        this.espacioService = espacioService;
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

    @PutMapping("/reservarEspacioTest")
    ResponseEntity<?> reservar(@RequestParam UUID idPersona, @RequestParam ArrayList<UUID> idEspacios){
        if (espacioService.todosEspacios().size()>0) {
            espacioService.reservarEspacio(idPersona, idEspacios,
                    LocalDate.of(2023, 3, 24), LocalTime.of(10, 30), LocalTime.of(14, 30),
                    TipoUso.DOCENCIA, 20, "Para dar clase bla bla bla");
        }
        return new ResponseEntity<>(espacioService.todosEspacios(), HttpStatus.OK);
    }
}
