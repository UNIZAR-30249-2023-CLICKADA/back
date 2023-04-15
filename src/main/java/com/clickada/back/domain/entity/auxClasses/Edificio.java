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
    protected double porcentajeUsoPermitido;
    List<LocalDate> diasNoReservables;
    //calendario -> lista dias cerrados
    //porcentaje uso maximo
    public Edificio(){}
    public Edificio(LocalTime horaInicio, LocalTime horaFin, List<LocalDate> diasNoReservables,
                    double porcentajeUsoPermitido) throws Exception {
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.diasNoReservables = diasNoReservables;
        if(porcentajeUsoPermitido>100 || porcentajeUsoPermitido<0) throw new Exception("Porcentaje No valido");
        this.porcentajeUsoPermitido = porcentajeUsoPermitido;
    }
}
