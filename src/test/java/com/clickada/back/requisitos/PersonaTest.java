package com.clickada.back.requisitos;

import com.clickada.back.domain.LoadPersonas;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonaTest {

    @Test
    void testConstructorPersona() {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol = Rol.DOCENTE_INVESTIGADOR;
        Persona persona = new Persona(nombre, eMail,"123", rol);
        assertEquals(nombre, persona.getNombre());
        assertEquals(eMail, persona.getEMail());

        assertNotNull(persona.getIdPersona());
    }

    @Test
    void testCambiarRol() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol1 = Rol.DOCENTE_INVESTIGADOR;
        Rol rol2 = Rol.GERENTE;

        Persona persona = new Persona(nombre, eMail,"123",rol1);
        try {
            persona.anyadirRol();
        }catch (Exception e){
            assertEquals(e.getMessage(),"No es Gerente, no puede tener segundo Rol");
        }

        persona.setAdscripcion(new Adscripcion());
        persona.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);

        persona.cambiarRol(rol2);
        assertEquals(rol2, persona.rolPrincipal());

        persona.anyadirRol();
        assertEquals(rol1, persona.rolSecundario());

        persona.cambiarRol(Rol.ESTUDIANTE);
        assertEquals(1,persona.getRoles().size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR);
        persona.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(1,persona.getRoles().size());

    }

    @Test
    void testCambioReservabilidad() throws Exception{
        Persona per = new Persona("Señor Tst","unico@mail.com", "123", Rol.TECNICO_LABORATORIO);
        Reservabilidad reservabilidad = new Reservabilidad(true, CategoriaReserva.AULA);
        Reservabilidad reservabilidad2 = new Reservabilidad(false, CategoriaReserva.LABORATORIO);
        Espacio espacioAula = new Espacio(reservabilidad,50,CategoriaEspacio.AULA);
        try {
            espacioAula.modificarReservabilidad(per,reservabilidad2);
        }catch (Exception e){
            assertEquals(e.getMessage(),"Si no es GERENTE no puede Modificar la Reservabilidad del Espacio");
        }
       per.cambiarRol(Rol.GERENTE);
        espacioAula.modificarReservabilidad(per,reservabilidad2);
        assertFalse(espacioAula.getReservabilidad().reservable);

    }
    @Test
    void asignarCorrectoAEspacio() throws Exception { // RF-14
        Reservabilidad reservabilidad = new Reservabilidad(true, CategoriaReserva.AULA);
        Espacio espacioAula = new Espacio(reservabilidad,50,CategoriaEspacio.AULA);
        Espacio espacioSalasComunes = new Espacio(reservabilidad,50,CategoriaEspacio.SALA_COMUN);
        Espacio espacioDespacho = new Espacio(reservabilidad,50,CategoriaEspacio.DESPACHO);
        Espacio espacioSeminarios = new Espacio(reservabilidad,50,CategoriaEspacio.SEMINARIO);
        Espacio espacioLaboratorios = new Espacio(reservabilidad,50,CategoriaEspacio.LABORATORIO);
        //Personas
        String nombre = "Juan";
        String eMail = "juan@clickada.com";

        Persona investigador_contratrado = new Persona(nombre, eMail,"123",Rol.INVESTIGADOR_CONTRATADO);
        Persona docente_investigador = new Persona(nombre, eMail,"123",Rol.DOCENTE_INVESTIGADOR);
        Persona gerenteYdocente_investigador = new Persona(nombre, eMail,"123",Rol.GERENTE);
        Persona personaNoValida =  new Persona(nombre, eMail,"123",Rol.TECNICO_LABORATORIO);
        gerenteYdocente_investigador.anyadirRol();
        //Propietarios
        PropietarioEspacio propietarioEina = new PropietarioEspacio(Eina.EINA);
        PropietarioEspacio propietarioDepartamento = new PropietarioEspacio(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);
        PropietarioEspacio propietarioPersonas = new PropietarioEspacio(List.of(investigador_contratrado,docente_investigador,gerenteYdocente_investigador));

        assertTrue(espacioAula.asignarAEspacio(propietarioEina));
        assertTrue(espacioSalasComunes.asignarAEspacio(propietarioEina));
        assertFalse(espacioAula.asignarAEspacio(propietarioDepartamento));
        assertFalse(espacioAula.asignarAEspacio(propietarioPersonas));
        assertFalse(espacioSalasComunes.asignarAEspacio(propietarioDepartamento));
        assertFalse(espacioSalasComunes.asignarAEspacio(propietarioPersonas));

        assertTrue(espacioDespacho.asignarAEspacio(propietarioDepartamento));
        assertTrue(espacioDespacho.asignarAEspacio(propietarioPersonas));
        assertFalse(espacioDespacho.asignarAEspacio(propietarioEina));

        try {
            PropietarioEspacio propietarioPersonasNoValido = new PropietarioEspacio(List.of(personaNoValida));
        }catch (Exception e){
            assertEquals(e.getMessage(),"Para ser propietario de espacio se tiene que ser DOCENTE_INVESTIGADOR o " +
                    "INVESTIGADOR_CONTRATADO");
        }

        assertTrue(espacioSeminarios.asignarAEspacio(propietarioDepartamento));
        assertTrue(espacioSeminarios.asignarAEspacio(propietarioEina));
        assertTrue(espacioLaboratorios.asignarAEspacio(propietarioDepartamento));
        assertTrue(espacioLaboratorios.asignarAEspacio(propietarioEina));
        assertFalse(espacioSeminarios.asignarAEspacio(propietarioPersonas));
        assertFalse(espacioLaboratorios.asignarAEspacio(propietarioPersonas));


    }
}