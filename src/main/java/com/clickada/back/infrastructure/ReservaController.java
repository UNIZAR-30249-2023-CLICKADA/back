package com.clickada.back.infrastructure;

import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ReservaController {
    ReservaService reservaService;
    PersonaService personaService;

    @Autowired
    public ReservaController(ReservaService reservaService, PersonaService personaService){
        this.reservaService = reservaService;
        this.personaService = personaService;
    }

    @PutMapping("/reservasVivas")
        /* Temp - habrá que decidir si unicamente del día actual  o de todos los dias posteriores */
    ResponseEntity<?> obtenerReservasVivas(@RequestParam UUID idPersona){
        try {
            return new ResponseEntity<>(reservaService.obtenerReservasVivas(idPersona), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
