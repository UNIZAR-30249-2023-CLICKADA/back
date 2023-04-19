package com.clickada.back.domain.entity.auxClasses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Edificio {
    @Id
    private UUID idEdificio;

    //Horario disponible de reserva
    protected LocalTime horaInicio;
    protected LocalTime horaFin;
    protected double porcentajeUsoPermitido;
    List<LocalDate> diasNoReservables;
    public Edificio(LocalTime horaInicio, LocalTime horaFin, List<LocalDate> diasNoReservables,
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
