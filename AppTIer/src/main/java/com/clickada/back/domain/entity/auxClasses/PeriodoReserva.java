package com.clickada.back.domain.entity.auxClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeriodoReserva implements Serializable {
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public boolean periodosCompatibles(PeriodoReserva periodoReserva){
        return !((periodoReserva.getHoraInicio().isBefore(horaFin) &&
                periodoReserva.getHoraInicio().isAfter(horaInicio)) ||
                (periodoReserva.getHoraFin().isBefore(horaFin) &&
                periodoReserva.getHoraFin().isAfter(horaInicio)) || (
                periodoReserva.getHoraFin().equals(horaFin) &&
                periodoReserva.getHoraInicio().equals(horaInicio)
                ));
    }
}
