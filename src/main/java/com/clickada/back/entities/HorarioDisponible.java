package com.clickada.back.entities;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class HorarioDisponible {
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private DayOfWeek diaSemana;

    public HorarioDisponible(LocalTime horaInicio, LocalTime horaFin, DayOfWeek diaSemana) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.diaSemana = diaSemana;
    }

    public boolean estaDisponible(LocalTime hora) {
        return hora.isAfter(horaInicio) && hora.isBefore(horaFin);
    }

    public Duration getDuracion() {
        return Duration.between(horaInicio, horaFin);
    }

    public void actualizarHorario(Reserva reserva) {
        Duration duracionReserva = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());
        horaInicio = reserva.getHoraFin();
        horaFin = horaInicio.plus(getDuracion().minus(duracionReserva));
    }

    public void mostrarHorario() {
        System.out.println("Horario disponible para el día " + diaSemana + ": " + horaInicio + " - " + horaFin);
    }
}