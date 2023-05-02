package com.clickada.back.requisitos;

import com.clickada.back.application.EspacioService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.application.ReservaService;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.CategoriaReserva;
import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void aptoParaCambiar() throws Exception {
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.GERENTE,null) ;
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.ESTUDIANTE,null) ;
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante);

        assertTrue(personaService.aptoParaCambiar(gerente.getIdPersona()));
        assertFalse(personaService.aptoParaCambiar(estudiante.getIdPersona()));

    }
    @Test
    void loginPersona() throws Exception {
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.GERENTE,null) ;
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.ESTUDIANTE,null) ;
        when(personaRepository.findByeMail(any())).thenReturn(null).thenReturn(gerente).thenReturn(estudiante);
        when(personaRepository.getById(any())).thenReturn(gerente).thenReturn(estudiante);

        assertNull(personaService.loginPersona(gerente.getEMail(),gerente.getContrasenya()));
        assertNull(personaService.loginPersona(estudiante.getEMail(),estudiante.getContrasenya()));
        assertNotNull(personaService.loginPersona(estudiante.getEMail(),estudiante.getContrasenya()));
    }

    @Test
    void permisos_de_reserva() throws Exception {
        Persona estudiante = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.ESTUDIANTE,null) ;
        Persona conserje = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.CONSERJE,null) ;
        Persona TECNICO_LABORATORIO = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.TECNICO_LABORATORIO, Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS) ;
        Persona DOCENTE_INVESTIGADOR = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.DOCENTE_INVESTIGADOR, Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES) ;
        Persona INVESTIGADOR_CONTRATADO = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.INVESTIGADOR_CONTRATADO,Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS) ;
        Persona gerente = new Persona("Señor Tst","unico@mail.com", "1232",
                Rol.GERENTE,null) ;
        when(personaRepository.existsById(any())).thenReturn(true);
        when(personaRepository.getById(any())).thenReturn(estudiante)
                .thenReturn(conserje)
                .thenReturn(INVESTIGADOR_CONTRATADO)
                .thenReturn(TECNICO_LABORATORIO)
                .thenReturn(DOCENTE_INVESTIGADOR)
                .thenReturn(gerente);
        // Act
        List<CategoriaReserva> result = personaService.permisosDeReserva(estudiante.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertEquals(1,result.size());

        result = personaService.permisosDeReserva(conserje.getIdPersona());
        // Assert
        assertTrue(result.contains(CategoriaReserva.SALA_COMUN));
        assertTrue(result.contains(CategoriaReserva.AULA));
        assertTrue(result.contains(CategoriaReserva.LABORATORIO));
        assertTrue(result.contains(CategoriaReserva.SEMINARIO));
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
