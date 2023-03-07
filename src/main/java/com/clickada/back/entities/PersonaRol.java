package com.clickada.back.entities;

import com.clickada.back.valueObject.Rol;

import java.util.ArrayList;

public class PersonaRol {
    public ArrayList<Rol> roles;

    public PersonaRol(Rol rol){
        roles.add(rol);
    }
    public void cambiarRol(Rol nuevoRol){
        roles.clear();
        roles.add(nuevoRol);
    }
    public void anyadirRol() throws Exception {   //Solo si es gerente puede tener un rol secundario
                                            // y tiene que ser docente-investigador
        if(roles.get(0).equals(Rol.GERENTE)){
            roles.add(Rol.DOCENTE_INVESTIGADOR);
        }
        else {
            throw new Exception("No es Gerente, no puede tener segundo Rol");
        }
    }
}
