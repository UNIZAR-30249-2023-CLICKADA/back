package com.clickada.back.requisitos;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@SpringBootTest
public class TestServicios {
    @InjectMocks
    PersonaService personaService;
    @Mock
    ReservaRepository reservaRepository;
    @Mock
    PersonaRepository personaRepository;
    @Mock
    EspacioRepository espacioRepository;
    @InjectMocks
    EspacioService espacioService;
    @InjectMocks
    ReservaService reservaService;

    @Test
    void aptoParaCambiar(){
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.GERENTE) ;
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.ESTUDIANTE) ;
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante);

        assertTrue(personaService.aptoParaCambiar(gerente.getIdPersona()));
        assertFalse(personaService.aptoParaCambiar(estudiante.getIdPersona()));

    }
    @Test
    void loginPersona(){
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.GERENTE) ;
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.ESTUDIANTE) ;
        when(personaRepository.findByeMail(any())).thenReturn(null).thenReturn(gerente).thenReturn(estudiante);
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante);

        assertFalse(personaService.loginPersona(gerente.getEMail(),gerente.getContrasenya()));
        assertFalse(personaService.loginPersona(estudiante.getEMail(),estudiante.getContrasenya()));
        assertTrue(personaService.loginPersona(estudiante.getEMail(),estudiante.getContrasenya()));
    }

    @Test
    void permisos_de_reserva(){
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.ESTUDIANTE) ;
        Persona conserje = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.CONSERJE) ;
        Persona TECNICO_LABORATORIO = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.TECNICO_LABORATORIO) ;
        Persona DOCENTE_INVESTIGADOR = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.DOCENTE_INVESTIGADOR) ;
        Persona INVESTIGADOR_CONTRATADO = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.INVESTIGADOR_CONTRATADO) ;
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.GERENTE) ;
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(estudiante.getIdPersona())).thenReturn(estudiante);
        when(personaRepository.getById(conserje.getIdPersona())).thenReturn(conserje);
        when(personaRepository.getById(TECNICO_LABORATORIO.getIdPersona())).thenReturn(TECNICO_LABORATORIO);
        when(personaRepository.getById(DOCENTE_INVESTIGADOR.getIdPersona())).thenReturn(DOCENTE_INVESTIGADOR);
        when(personaRepository.getById(INVESTIGADOR_CONTRATADO.getIdPersona())).thenReturn(INVESTIGADOR_CONTRATADO);
        when(personaRepository.getById(gerente.getIdPersona())).thenReturn(gerente);
        // Act
        List<CategoriaReserva> result = personaService.permisosDeReserva(estudiante.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));

        result = personaService.permisosDeReserva(conserje.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertTrue(result.contains(CategoriaReserva.AULA));
        result = personaService.permisosDeReserva(INVESTIGADOR_CONTRATADO.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertTrue(result.contains(CategoriaReserva.AULA));
        assertTrue(result.contains(CategoriaReserva.LABORATORIO));
        result = personaService.permisosDeReserva(TECNICO_LABORATORIO.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        result = personaService.permisosDeReserva(DOCENTE_INVESTIGADOR.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertTrue(result.contains(CategoriaReserva.AULA));
        assertTrue(result.contains(CategoriaReserva.LABORATORIO));
        result = personaService.permisosDeReserva(gerente.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertTrue(result.contains(CategoriaReserva.AULA));
        assertTrue(result.contains(CategoriaReserva.LABORATORIO));
        assertTrue(result.contains(CategoriaReserva.SEMINARIO));
    }
}