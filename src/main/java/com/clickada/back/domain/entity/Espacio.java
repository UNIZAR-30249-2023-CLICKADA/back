package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Espacio extends Edificio {
    @Id
    UUID idEspacio;

    CategoriaEspacio categoriaEspacio;

    double tamanyo; //Tamaño del espacio en m2

    @Transient
    Reservabilidad reservabilidad;

    int numMaxOcupantes;

    @Transient
    ArrayList<LocalDate> diasNoReservables;

    double porcentajeUsoPermitido;

    @Transient
    PropietarioEspacio propietarioEspacio;

    //HorarioDisponible horarioDisponible;

    public Espacio(Reservabilidad reservabilidad, double tamanyo, int maxOcupantes, CategoriaEspacio categoriaEspacio){
        super();
        idEspacio = UUID.randomUUID();
        this.reservabilidad = reservabilidad;
        this.tamanyo = tamanyo;
        this.categoriaEspacio = categoriaEspacio;
        this.porcentajeUsoPermitido = porcentajeUsoEdificio;
        this.numMaxOcupantes = maxOcupantes;
    }

    public void modificarReservabilidad(Persona persona, boolean reservable, CategoriaReserva categoriaReserva) throws Exception {
        if(!persona.roles.get(0).equals(Rol.GERENTE)) {
            throw new Exception("Si no es GERENTE no puede Modificar la Reservabilidad del Espacio");
        }
        this.reservabilidad.reservable = reservable;
        if(reservable){
            this.reservabilidad.categoriaReserva = categoriaReserva;
        }
    }
    public boolean asignarAEspacio(PropietarioEspacio propietarioEspacio){
        if((this.categoriaEspacio.equals(CategoriaEspacio.AULA) ||
                this.categoriaEspacio.equals(CategoriaEspacio.SALA_COMUN) ||
        this.categoriaEspacio.equals(CategoriaEspacio.SEMINARIO) ||
        this.categoriaEspacio.equals(CategoriaEspacio.LABORATORIO))
         && propietarioEspacio.getIndexPropietario()==0){ // EINA
            this.propietarioEspacio = propietarioEspacio;
            return true;
        }else if(propietarioEspacio.getIndexPropietario()==1 &&
                (this.categoriaEspacio.equals(CategoriaEspacio.DESPACHO) ||
                this.categoriaEspacio.equals(CategoriaEspacio.SEMINARIO) ||
                this.categoriaEspacio.equals(CategoriaEspacio.LABORATORIO))){ // Departamento
            this.propietarioEspacio = propietarioEspacio;
            return true;
        } else if(propietarioEspacio.getIndexPropietario() == 2 &&
                this.categoriaEspacio.equals(CategoriaEspacio.DESPACHO)){

            for(Persona persona : propietarioEspacio.personas){
                if(!persona.asignable()){
                    return false;
                }
            }
            this.propietarioEspacio = propietarioEspacio;
            return true;
        }
        return false;
    }

    public boolean modificarPorcentajeOcupacion(Persona persona, int porcentaje){
        if(persona.getRoles().get(0).equals(Rol.GERENTE)){
            this.porcentajeUsoPermitido = porcentaje;
            return true;
        }
        return false;
    }

}
