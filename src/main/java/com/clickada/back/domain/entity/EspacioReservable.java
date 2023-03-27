package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
@Setter
public class EspacioReservable extends Edificio {
    @Id
    UUID idEspacio; //o algo así intuyo

    CategoriaEspacio categoriaEspacio;

    Long tamanyo; //Tamaño del espacio en m2

    @Transient

    Reservabilidad reservabilidad;
    //HorarioDisponible horarioDisponible;
    //porcentaje uso maximo

    public EspacioReservable(Reservabilidad reservabilidad, Long tamanyo, CategoriaEspacio categoriaEspacio){
        super();
        idEspacio = UUID.randomUUID();
        this.reservabilidad = reservabilidad;
        this.tamanyo = tamanyo;
        this.categoriaEspacio = categoriaEspacio;
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


}
