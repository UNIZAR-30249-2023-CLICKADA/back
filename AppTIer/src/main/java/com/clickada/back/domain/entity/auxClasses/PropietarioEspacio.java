package com.clickada.back.domain.entity.auxClasses;

import com.clickada.back.domain.entity.Persona;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PropietarioEspacio implements Serializable {
    public Eina eina;
    public Departamento departamento;

    public List<UUID> personas;
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
        List<UUID> idPersonas = new ArrayList<>();
        for(Persona persona : personas){
            if(!persona.asignable()){
                throw new Exception("Para ser propietario de espacio se tiene que ser DOCENTE_INVESTIGADOR o " +
                        "INVESTIGADOR_CONTRATADO");
            }
            idPersonas.add(persona.getIdPersona());
        }
        this.personas = idPersonas;
        this.indexPropietario = 2;
    }
    public boolean esDepartamento(){
        return this.indexPropietario==1;
    }
    public boolean esEina(){
        return this.indexPropietario==0;
    }
    public boolean esPersonas(){
        return this.indexPropietario==2;
    }
}
