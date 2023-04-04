package com.clickada.back.domain.entity.auxClasses;

import com.clickada.back.domain.entity.Persona;

import java.util.List;

public class PropietarioEspacio {
    public String eina = "EINA";
    public Departamento departamento;

    public List<Persona> personas;
    int indexPropietario;

    public PropietarioEspacio(String eina){
        if (eina.equals(this.eina)){
            this.indexPropietario=0;
        }
    }
    public PropietarioEspacio(Departamento departamento){
        this.departamento = departamento;
        this.indexPropietario = 1;
    }
    public  PropietarioEspacio(List<Persona> personas) throws Exception {
        for(Persona persona : personas){
            if(!persona.getRoles().get(0).equals(Rol.DOCENTE_INVESTIGADOR) &&
                    !persona.getRoles().get(0).equals(Rol.INVESTIGADOR_CONTRATADO)){
                throw new Exception("Para ser propietario de espacio se tiene que ser DOCENTE_INVESTIGADOR o " +
                        "INVESTIGADOR_CONTRATADO");
            }
        }
        this.personas = personas;
        this.indexPropietario = 2;
    }
    public int getIndexPropietario(){
        return indexPropietario;
    }
}
