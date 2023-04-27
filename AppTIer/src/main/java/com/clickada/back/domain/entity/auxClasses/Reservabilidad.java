package com.clickada.back.domain.entity.auxClasses;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Slf4j
@NoArgsConstructor
public class Reservabilidad implements Serializable {
    public CategoriaReserva categoriaReserva;
    public boolean reservable;

    // porcentaje maximo de reserva

    public Reservabilidad(boolean reservable, CategoriaReserva categoriaReserva) throws Exception {
        if(categoriaReserva.equals(CategoriaReserva.DESPACHO) && reservable){
            throw new Exception("Los despachos no pueden ser reservables");
        }
        this.reservable = reservable;
        this.categoriaReserva = categoriaReserva;

    }
    public Reservabilidad(boolean reservable, String categoriaReserva) throws Exception {
        CategoriaReserva categoriaReserva1 = CategoriaReserva.getCategoriaByString(categoriaReserva);
        if(categoriaReserva1==null) {
            throw new Exception("No existe esa Categoria de Reserva");
        }
        if(categoriaReserva1.equals(CategoriaReserva.DESPACHO) && reservable){
            throw new Exception("Los despachos no pueden ser reservables");
        }
        this.reservable = reservable;

        this.categoriaReserva = categoriaReserva1;

    }

}
