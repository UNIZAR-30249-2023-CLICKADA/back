package com.clickada.back.entities;

import com.clickada.back.valueObject.Departamento;
import com.clickada.back.valueObject.Rol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persona {
    public String nombre;
    public String eMail;
    public PersonaRol personaRol;
    public Departamento departamento;

    public void cambiarRol(Rol nuevoRol){
        personaRol.cambiarRol(nuevoRol);
    }

    public void anyadirRol() throws Exception {
        personaRol.anyadirRol();
    }
}
