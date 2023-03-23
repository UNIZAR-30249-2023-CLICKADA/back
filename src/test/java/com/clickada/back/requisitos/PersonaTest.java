package com.clickada.back.requisitos;

import com.clickada.back.domain.LoadPersonas;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.junit.jupiter.api.Test;

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
        //assertEquals(rol, persona.getPersonaRol().roles.get(0));
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


        persona.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);



        persona.cambiarRol(rol2);
        assertEquals(rol2, persona.getRoles().get(0));

        persona.anyadirRol();
        assertEquals(rol1, persona.getRoles().get(1));

        persona.cambiarRol(Rol.ESTUDIANTE);
        assertEquals(1,persona.getRoles().size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR);
        persona.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(1,persona.getRoles().size());

    }

    @Test
    void testLoginPersona() throws Exception{
        Persona per = new Persona("Señor Mail Repetido","unico@mail.com", "123", Rol.TECNICO_LABORATORIO);


    }
}