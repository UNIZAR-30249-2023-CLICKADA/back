package com.clickada.back.requisitos;

import com.clickada.back.entities.Adscripcion;
import com.clickada.back.entities.Persona;
import com.clickada.back.valueObject.Departamento;
import com.clickada.back.valueObject.Rol;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class PersonaTest {

    @Test
    void testConstructorPersona() {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol = Rol.DOCENTE_INVESTIGADOR;

        Persona persona = new Persona(nombre, eMail, rol);

        assertEquals(nombre, persona.nombre);
        assertEquals(eMail, persona.eMail);
        assertEquals(rol, persona.personaRol.roles.get(0));
        assertNotNull(persona.idPersona);
    }

    @Test
    void testCambiarRol() throws Exception {
        String nombre = "Juan";
        String eMail = "juan@clickada.com";
        Rol rol1 = Rol.DOCENTE_INVESTIGADOR;
        Rol rol2 = Rol.GERENTE;

        Persona persona = new Persona(nombre, eMail, rol1);
        try {
            persona.anyadirRol();
        }catch (Exception e){
            assertEquals(e.getMessage(),"No es Gerente, no puede tener segundo Rol");
        }


        persona.adscripcionADepartamento(Departamento.INFORMATICA_E_INGENIERIA_DE_SISTEMAS);


        persona.cambiarRol(rol2);
        assertEquals(rol2, persona.personaRol.roles.get(0));

        persona.anyadirRol();
        assertEquals(rol1, persona.personaRol.roles.get(1));

        persona.cambiarRol(Rol.ESTUDIANTE);
        assertEquals(1,persona.personaRol.roles.size());
        persona.cambiarRol(Rol.DOCENTE_INVESTIGADOR);
        persona.adscripcionADepartamento(Departamento.INGENIERIA_ELECTRONICA_Y_COMUNICACIONES);
        assertEquals(1,persona.personaRol.roles.size());

    }
}