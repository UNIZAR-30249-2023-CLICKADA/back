package com.clickada.back.domain;


import com.clickada.back.domain.entity.auxClasses.PersonaRol;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.UUID;

@Configuration
public class LoadPersonas {
    private static final Logger log = LoggerFactory.getLogger(LoadPersonas.class);

    @Bean
    CommandLineRunner initDatabase(PersonaRepository personaRepository) {

        Persona persona = new Persona("Pepe Agustin","pepe@gmail.com", "123", Rol.CONSERJE);
        Persona persona1 = new Persona("Amanda Garcia","amanda@gmail.com","321",Rol.GERENTE);
        personaRepository.deleteAllInBatch();//Limpiar primero para evitar duplicados
        personaRepository.save(persona);
        personaRepository.save(persona1);
        Persona p3 = new Persona("Carlos Altea","unico@mail.com", "321", Rol.TECNICO_LABORATORIO);
        personaRepository.save(p3);

        return null;
    }
}
