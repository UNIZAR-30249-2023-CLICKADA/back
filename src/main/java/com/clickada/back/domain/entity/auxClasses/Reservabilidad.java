package com.clickada.back.domain.entity.auxClasses;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;

@Embeddable
@Slf4j
@NoArgsConstructor
public class Reservabilidad {
    public CategoriaReserva categoriaReserva;
    public boolean reservable;

    // porcentaje maximo de reserva

    public Reservabilidad(boolean reservable, CategoriaReserva categoriaReserva){
        this.reservable = reservable;
        if(reservable){
            this.categoriaReserva = categoriaReserva;
        }
    }

}
