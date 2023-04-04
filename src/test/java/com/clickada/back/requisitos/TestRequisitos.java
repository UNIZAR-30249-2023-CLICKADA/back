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
}
