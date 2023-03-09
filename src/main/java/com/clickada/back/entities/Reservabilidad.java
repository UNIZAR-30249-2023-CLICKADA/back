package com.clickada.back.entities;

import com.clickada.back.valueObject.CategoriaReserva;

public class Reservabilidad {
    public CategoriaReserva categoriaReserva;
    public boolean reservable;

    // Horario reserva disponibles


    public Reservabilidad(boolean reservable,CategoriaReserva categoriaReserva){
        this.reservable = reservable;
        if(reservable){
            this.categoriaReserva = categoriaReserva;
        }
    }
}
