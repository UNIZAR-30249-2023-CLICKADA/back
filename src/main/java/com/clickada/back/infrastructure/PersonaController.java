package com.clickada.back.infrastructure;

import com.clickada.back.application.PersonaService;
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

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
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

}
