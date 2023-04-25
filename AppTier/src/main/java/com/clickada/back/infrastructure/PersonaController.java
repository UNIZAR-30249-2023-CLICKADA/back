package com.clickada.back.infrastructure;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.entity.auxClasses.Reservabilidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PersonaController {

    PersonaService personaService;
    EspacioService espacioService;

    @Autowired
    public PersonaController(PersonaService personaService, EspacioService espacioService,
                             EspacioRepository espacioRepository) {
        this.personaService = personaService;
        this.espacioService = espacioService;
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


    @PutMapping("/cambiarReservabilidad")
    ResponseEntity<?> cambiarReservabilidad(@RequestParam UUID idPersona,  @RequestParam UUID idEspacio,
                                            @RequestParam boolean reservable, @RequestParam String categoriaReserva) throws Exception {
        if (personaService.aptoParaCambiar(idPersona)){
            try{
                if(espacioService.cambiarReservabilidadEspacio(idEspacio,new Reservabilidad(reservable,categoriaReserva),idPersona)){
                    return new ResponseEntity<>("Reservabilidad cambiada correctamente", HttpStatus.OK);
                }
                return new ResponseEntity<>("No existe el espacio para cambiar la reservabilidad",HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>("La persona no es apta para cambiar nada",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/loginPersona")
    /* Las llamadas desde el frontend deben convertir a Base64 la contraseña antes de hacer el request */
    ResponseEntity<?> loginPersona(@RequestParam String email, @RequestParam String pass){
        if (personaService.loginPersona(email,pass)!=null){
            return new ResponseEntity<>("True",HttpStatus.OK);
        }
        else { return  new ResponseEntity<>("Login fallido",HttpStatus.BAD_REQUEST); }
    }

    @PutMapping("/permisosReserva")
    ResponseEntity<?> permisosDeReserva(@RequestParam UUID id){
        return new ResponseEntity<>(personaService.permisosDeReserva(id),HttpStatus.OK);
    }


}
