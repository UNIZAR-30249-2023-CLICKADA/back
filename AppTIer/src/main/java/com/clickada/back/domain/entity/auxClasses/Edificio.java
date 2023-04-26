package com.clickada.back.domain.entity.auxClasses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Edificio {
    @Id
    UUID idEdificio;

    //Horario disponible de reserva
    protected LocalTime horaInicio;
    protected LocalTime horaFin;
    protected double porcentajeUsoPermitido;
    ArrayList<LocalDate> diasNoReservables;
    public Edificio(LocalTime horaInicio, LocalTime horaFin, ArrayList<LocalDate> diasNoReservables,
                    double porcentajeUsoPermitido) throws Exception {
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.diasNoReservables = diasNoReservables;
        cambiarPorcentajeEdificio(porcentajeUsoPermitido);
    }
    public void cambiarPorcentajeEdificio(double porcentajeNuevo) throws Exception {
        if(porcentajeNuevo>100 || porcentajeNuevo<0) throw new Exception("Porcentaje No valido");
        this.porcentajeUsoPermitido = porcentajeNuevo;
    }
}
