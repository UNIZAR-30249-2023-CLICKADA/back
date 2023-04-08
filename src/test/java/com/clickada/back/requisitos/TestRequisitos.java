package com.clickada.back.requisitos;

import com.clickada.back.application.PersonaService;
import com.clickada.back.domain.entity.auxClasses.Adscripcion;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.auxClasses.Departamento;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

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

}
