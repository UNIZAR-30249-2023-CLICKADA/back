package com.clickada.back.requisitos;

import com.clickada.back.entities.Persona;
import com.clickada.back.valueObject.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class TestRequisitos {

    @Test
    void requisito2(){
        Persona persona = new Persona("Pepe","pepe@gmail.com", Rol.CONSERJE);

        Assert.state(persona.eMail.equals("pepe@gmail.com"),"Conserje");
    }
}
