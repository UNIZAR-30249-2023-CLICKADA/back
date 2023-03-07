package com.clickada.back.entities;

import com.clickada.back.valueObject.Departamento;
import com.clickada.back.valueObject.Rol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Persona {
    public String nombre;
    public String eMail;
    public PersonaRol personaRol;
    public PersonaDepartamento personaDepartamento;
    private boolean departamentoDisponible;

    public Persona(String nombre, String eMail, Rol rol){
        this.nombre = nombre;
        this.eMail = eMail;
        this.departamentoDisponible = optaADepartamento(rol);
        this.personaRol = new PersonaRol(rol);
    }
    public void cambiarRol(Rol nuevoRol){
        personaRol.cambiarRol(nuevoRol);
        // gestionar el nuevo rol con los departamentos
        /*if(departamentoDisponible || !optaADepartamento(nuevoRol)){

        }*/
    }

    public void anyadirRol() throws Exception {
        personaRol.anyadirRol();
        departamentoDisponible = true;
    }
    private boolean optaADepartamento(Rol rol){
        if(rol.equals(Rol.DOCENTE_INVESTIGADOR) ||
                rol.equals(Rol.INVESTIGADOR_CONTRATADO) ||
                rol.equals(Rol.TECNICO_LABORATORIO)){
            return true;
        }
        return false;
    }
}
