package com.clickada.back.domain.entity.auxClasses;

import com.clickada.back.domain.entity.Persona;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Getter
@NoArgsConstructor
public class PropietarioEspacio implements Serializable {
    public Eina eina;
    public Departamento departamento;

    public List<Persona> personas;
    int indexPropietario;

    public PropietarioEspacio(Eina eina){
        this.eina = eina;
        this.indexPropietario=0;
    }
    public PropietarioEspacio(Departamento departamento){
        this.departamento = departamento;
        this.indexPropietario = 1;
    }
    public  PropietarioEspacio(List<Persona> personas) throws Exception {
        for(Persona persona : personas){
            if(!persona.asignable()){
                throw new Exception("Para ser propietario de espacio se tiene que ser DOCENTE_INVESTIGADOR o " +
                        "INVESTIGADOR_CONTRATADO");
            }
        }
        this.personas = personas;
        this.indexPropietario = 2;
    }
}
