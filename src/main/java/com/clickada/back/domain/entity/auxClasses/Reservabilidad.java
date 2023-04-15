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
    public Reservabilidad(boolean reservable, String categoriaReserva) throws Exception {
        CategoriaReserva categoriaReserva1 = CategoriaReserva.getCategoriaByString(categoriaReserva);
        if(categoriaReserva1==null) {
            throw new Exception("No existe esa Categoria de Reserva");
        }
        this.reservable = reservable;
        if(reservable){
            this.categoriaReserva = categoriaReserva1;
        }
    }

}
