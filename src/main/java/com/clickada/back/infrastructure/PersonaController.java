package com.clickada.back.infrastructure;

import com.clickada.back.application.EspacioReservableService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PersonaController {

    PersonaService personaService;
    EspacioReservableService espacioReservableService;
    private final EspacioRepository espacioRepository;

    @Autowired
    public PersonaController(PersonaService personaService, EspacioReservableService espacioReservableService,
                             EspacioRepository espacioRepository) {
        this.personaService = personaService;
        this.espacioReservableService = espacioReservableService;
        this.espacioRepository = espacioRepository;
    }

    @PutMapping("/cambiarRol")
    ResponseEntity<?> cambiarRolPersona(@RequestParam UUID idPersona, @RequestParam String rol){
        if(personaService.cambiarRol(idPersona,rol)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>("Persona no encontrada, no se ha podido cambiar su Rol",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/todasPersonas")
    ResponseEntity<?> todasPersonas(){
        return new ResponseEntity<>(personaService.todasPersonas(),HttpStatus.OK);
    }

    @GetMapping("/todosEspacios")
    ResponseEntity<?> todosEspacios(){
        return new ResponseEntity<>(espacioReservableService.todosEspacios(),HttpStatus.OK);
    }

    @PutMapping("/cambiarReservabilidad")
    ResponseEntity<?> cambiarReservabilidad(@RequestParam UUID idPersona,  @RequestParam UUID idEspacio, @RequestParam boolean reservable){
        if (personaService.aptoParaCambiar(idPersona)){
            if(espacioReservableService.cambiarReservabilidadEspacio(idEspacio,reservable)){
                return new ResponseEntity<>("Reservabilidad cambiada correctamente", HttpStatus.OK);
            }
            return new ResponseEntity<>("No existe el espacio para cambiar la reservabilidad",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("La persona no es apta para cambiar nada",HttpStatus.BAD_REQUEST);
    }

}
