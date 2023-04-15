package com.clickada.back.requisitos;

import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import com.clickada.back.domain.entity.Persona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
public class TestRequisitos {

    PersonaService personaService;
    @Test
    void requisito2(){
        Persona persona = new Persona("Pepe","pepe@gmail.com","123", Rol.CONSERJE);

        Assert.state(persona.getEMail().equals("pepe@gmail.com"),"Conserje");
    }

    @Test
    public void requisito3(){
        Persona per = new Persona("Señor Tst","unico@mail.com", "123",
                Rol.getRolByString("Estudiante")) ;
        assertEquals(Rol.ESTUDIANTE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Conserje"));
        assertEquals(Rol.CONSERJE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Investigador"));
        assertEquals(Rol.INVESTIGADOR_CONTRATADO,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Docente"));
        assertEquals(Rol.DOCENTE_INVESTIGADOR,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Tecnico"));
        assertEquals(Rol.TECNICO_LABORATORIO,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("Gerente"));
        assertEquals(Rol.GERENTE,per.rolPrincipal());

        per.cambiarRol(Rol.getRolByString("RolErroneo"));
        //Debe mantener el anterior
        assertEquals(Rol.GERENTE,per.rolPrincipal());

    }

    @Test
    void requisito4_5_6() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol_docente = Rol.DOCENTE_INVESTIGADOR;
        Rol rol_gerente = Rol.GERENTE;

        Persona persona = new Persona(nombre, eMail,"123",rol_docente);
        try {
            persona.anyadirRol();
        }catch (Exception e){
            Assertions.assertEquals(e.getMessage(),"No es Gerente, no puede tener segundo Rol");
        }

        persona.setAdscripcion(new Adscripcion());
        persona.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        persona.cambiarRol(rol_gerente);
        Assertions.assertEquals(rol_gerente, persona.rolPrincipal());

        persona.anyadirRol();
        Assertions.assertEquals(rol_docente, persona.rolSecundario());

        persona.cambiarRol(Rol.ESTUDIANTE);
        Assertions.assertEquals(1,persona.getRoles().size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR);
        persona.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        Assertions.assertEquals(1,persona.getRoles().size());

    }
    @Test
    void requisito7(){
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona investigador = new Persona(nombre, eMail,"123",Rol.INVESTIGADOR_CONTRATADO);
        Persona docente = new Persona(nombre,eMail,"123",Rol.DOCENTE_INVESTIGADOR);
        Persona tecnico = new Persona(nombre,eMail,"123",Rol.TECNICO_LABORATORIO);

        investigador.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,investigador.getAdscripcion().departamento);

        docente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,docente.getAdscripcion().departamento);

        tecnico.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,tecnico.getAdscripcion().departamento);

    }
    @Test
    void requisito8_9() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona estudiante = new Persona(nombre, eMail,"123",Rol.ESTUDIANTE);
        Persona conderje = new Persona(nombre,eMail,"123",Rol.CONSERJE);
        Persona gerente = new Persona(nombre,eMail,"123",Rol.GERENTE);

        estudiante.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertNull(estudiante.getAdscripcion());

        conderje.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertNull(conderje.getAdscripcion());

        gerente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertNull(gerente.getAdscripcion());

        gerente.anyadirRol();
        gerente.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES,gerente.getAdscripcion().departamento);

        gerente.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        assertEquals(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS,gerente.getAdscripcion().departamento);

    }
    @Test
    public void requisito10() throws Exception {
        Edificio edificio = new Edificio(
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                List.of(LocalDate.of(2023,1,1)));
        Espacio espacio = new Espacio(new Reservabilidad(),150,
                CategoriaEspacio.SALA_COMUN,edificio);
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);
        Persona docente = new Persona("Ger","ger@clickada.es","1234",Rol.DOCENTE_INVESTIGADOR);
        Reservabilidad reservabilidad1 = new Reservabilidad(false, CategoriaReserva.LABORATORIO);
        Reservabilidad reservabilidad2 = new Reservabilidad(true,CategoriaReserva.AULA);

        espacio.modificarReservabilidad(gerente,reservabilidad1);
        assertEquals(false,espacio.getReservabilidad().reservable);
        assertEquals(reservabilidad1.categoriaReserva,espacio.getReservabilidad().categoriaReserva);

        espacio.modificarReservabilidad(gerente,reservabilidad2);
        assertEquals(true,espacio.getReservabilidad().reservable);
        assertEquals(CategoriaReserva.AULA,espacio.getReservabilidad().categoriaReserva);

        assertEquals(edificio.getHoraInicio(),espacio.getHoraInicio());
        espacio.modificarHorarioDisponible(gerente,
                LocalTime.of(9,0),
                LocalTime.of(19,0));
        assertNotEquals(edificio.getHoraFin(),espacio.getHoraFin());

        try{
            espacio.modificarHorarioDisponible(gerente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        }catch (Exception e){
            assertEquals("Las horas nuevas de reserva tienen que estar dentro del periodo de reseerva del Edificio",e.getMessage());
        }
        try{
            espacio.modificarHorarioDisponible(docente,
                    LocalTime.of(7,0),
                    LocalTime.of(21,0));
        }catch (Exception e){
            assertEquals("Si no es GERENTE no puede Modificar el horario de reserva del Espacio",e.getMessage());
        }

    }


    @Test
    public void testReservas() {
        Reserva r1 = new Reserva();
        Persona p = new Persona();
        Espacio e1 = new Espacio(new Reservabilidad(),150,
                CategoriaEspacio.SALA_COMUN);
        ArrayList<UUID> esp = new ArrayList<>();
        esp.add(e1.getIdEspacio());
        Reserva r2 = new Reserva(new PeriodoReserva(LocalTime.NOON,LocalTime.MIDNIGHT),p.getIdPersona(),
                TipoUso.DOCENCIA,esp,20,"DD",LocalDate.now());
        Persona gerente = new Persona("Ger","ger@clickada.es","1234",Rol.GERENTE);


    }

}
