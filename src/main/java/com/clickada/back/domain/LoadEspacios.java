package com.clickada.back.domain;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class LoadEspacios {
    private static final Logger log = LoggerFactory.getLogger(LoadEspacios.class);

    @Bean
    CommandLineRunner initDatabase2(EspacioRepository espacioRepository,EdificioRepository edificioRepository,
                                    PersonaRepository personaRepository,ReservaRepository reservaRepository) throws Exception {
        espacioRepository.deleteAllInBatch();//Limpiar primero para evitar duplicados
        edificioRepository.deleteAllInBatch();
        personaRepository.deleteAllInBatch();
        reservaRepository.deleteAllInBatch();

        Persona investigador = new Persona("Juan", "juan@clickada.com","123", Rol.INVESTIGADOR_CONTRATADO, Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona docente = new Persona("docente","docente@clickada.com","123",Rol.DOCENTE_INVESTIGADOR,Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Persona tecnico = new Persona("tecnico","tecnico@clickada.com","123",Rol.TECNICO_LABORATORIO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        Persona estudiante = new Persona("estudiante", "estudiante@clickada.com","123",Rol.ESTUDIANTE,null);
        Persona conserje = new Persona("conserje","conserje@clickada.com","123",Rol.CONSERJE,null);
        Persona gerente = new Persona("gerente","gerente@clickada.com","123",Rol.GERENTE,null);
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>(List.of(LocalDate.of(2023,1,1))),100);
        edificio.setIdEdificio(UUID.randomUUID());
        Espacio sala_comun = new Espacio(new Reservabilidad(true, CategoriaReserva.SALA_COMUN),150, 60,
                CategoriaEspacio.SALA_COMUN,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio aula = new Espacio(new Reservabilidad(false,CategoriaReserva.AULA),150, 60,
                CategoriaEspacio.AULA,edificio,new PropietarioEspacio(Eina.EINA));
        Espacio seminario = new Espacio(new Reservabilidad(false,CategoriaReserva.SEMINARIO),150, 60,
                CategoriaEspacio.SEMINARIO,edificio,new PropietarioEspacio(Eina.EINA));
        edificioRepository.save(edificio);

        espacioRepository.save(sala_comun);
        espacioRepository.save(aula);
        espacioRepository.save(seminario);

        personaRepository.save(investigador);
        personaRepository.save(docente);
        personaRepository.save(tecnico);
        personaRepository.save(estudiante);
        personaRepository.save(conserje);
        personaRepository.save(gerente);
        return null;
    }
}
