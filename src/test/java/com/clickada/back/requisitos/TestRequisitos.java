package com.clickada.back.requisitos;

import com.clickada.back.application.EspacioReservableService;
import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import com.clickada.back.domain.entity.Persona;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest
public class TestRequisitos {

    PersonaService personaService;
    @Test
    void requisito2(){
        Persona persona = new Persona("Pepe","pepe@gmail.com","123", Rol.CONSERJE);

        Assert.state(persona.getEMail().equals("pepe@gmail.com"),"Conserje");
    }
    @Test
    public void testAnyadirDepartamento() {
        // Crear objeto Adscripcion
        Adscripcion adscripcion = new Adscripcion(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS, adscripcion.departamento);

        // Añadir un departamento
        adscripcion.setDepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES, adscripcion.departamento);

    }

    @Test
    public void testStringRol(){
        Persona per = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.getRolByString("Estudiante")) ;
        assertEquals(Rol.ESTUDIANTE,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("Conserje"));
        assertEquals(Rol.CONSERJE,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("Investigador"));
        assertEquals(Rol.INVESTIGADOR_CONTRATADO,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("Docente"));
        assertEquals(Rol.DOCENTE_INVESTIGADOR,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("Tecnico"));
        assertEquals(Rol.TECNICO_LABORATORIO,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("Gerente"));
        assertEquals(Rol.GERENTE,per.getRoles().get(0));

        per.cambiarRol(Rol.getRolByString("RolErroneo"));
        //Debe mantener el anterior
        assertEquals(Rol.GERENTE,per.getRoles().get(0));

    }

    @Test
    public void testCambioReservabilidad() throws Exception {
        Espacio e = new Espacio(new Reservabilidad(),150,100,
                CategoriaEspacio.SALA_COMUN);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        e.modificarReservabilidad(gerente,false, CategoriaReserva.LABORATORIO);
        assertEquals(false,e.getReservabilidad().reservable);

        e.modificarReservabilidad(gerente,true,CategoriaReserva.AULA);
        assertEquals(true,e.getReservabilidad().reservable);

        assertEquals(CategoriaReserva.AULA,e.getReservabilidad().categoriaReserva);
    }

    @Test
    public void testReservas() {
        Reserva r1 = new Reserva();
        Persona p = new Persona();
        Espacio e1 = new Espacio(new Reservabilidad(),150,100,
                CategoriaEspacio.SALA_COMUN);
        ArrayList<UUID> esp = new ArrayList<>();
        esp.add(e1.getIdEspacio());
        Reserva r2 = new Reserva(new PeriodoReserva(LocalDate.now(), LocalTime.NOON,LocalTime.MIDNIGHT),p.getIdPersona(),
                TipoUso.DOCENCIA,esp,20,"DD");
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);


    }

}
