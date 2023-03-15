package com.clickada.back.repository.entities.auxClasses;

import com.clickada.back.valueObject.Rol;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class PersonaRol {
    public ArrayList<Rol> roles;

    public PersonaRol(Rol rol){
        roles = new ArrayList<>();
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
