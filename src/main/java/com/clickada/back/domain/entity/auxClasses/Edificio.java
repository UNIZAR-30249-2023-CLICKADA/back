package com.clickada.back.domain.entity.auxClasses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class Edificio {
    //Horario disponible de reserva
    protected LocalTime horaInicio;
    protected LocalTime horaFin;

    List<LocalDate> diasNoReservables;
    //calendario -> lista dias cerrados
    //porcentaje uso maximo
    public Edificio(){}
    public Edificio(LocalTime horaInicio, LocalTime horaFin, List<LocalDate> diasNoReservables){
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.diasNoReservables = diasNoReservables;
    }
}
