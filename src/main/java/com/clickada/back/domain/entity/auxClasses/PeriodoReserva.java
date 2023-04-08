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
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
