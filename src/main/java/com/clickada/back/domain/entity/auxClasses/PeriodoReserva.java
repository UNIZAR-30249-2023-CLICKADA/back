package com.clickada.back.domain.entity.auxClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeriodoReserva {
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public boolean periodosCompatibles(PeriodoReserva periodoReserva){
        return !((periodoReserva.getHoraInicio().isBefore(horaFin) &&
                periodoReserva.getHoraInicio().isAfter(horaInicio)) ||
                periodoReserva.getHoraFin().isBefore(horaFin) &&
                        periodoReserva.getHoraFin().isAfter(horaInicio));
        //hora inicio p1 esta entre la hora inicio p2 y hora fin p2
        //hora fin p1 esta entre hora inicio p2 y hora fin p2

    }
}
