package com.clickada.back.domain.entity.auxClasses;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
public class Reserva {
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private LocalDate fechaFin;
    private LocalTime horaFin;
    private UUID idPersona;
    private TipoUso tipoDeUso;
    private int numAsistentes;
    private String detallesReserva;
    private UUID idEspacio;


    public Reserva(LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, UUID idPersona,
                   TipoUso tipoDeUso,UUID idEspacio, int numAsistentes, String detallesReserva) {
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.fechaFin = fechaFin;
        this.horaFin = horaFin;
        this.idPersona = idPersona;
        this.tipoDeUso = tipoDeUso;
        this.numAsistentes = numAsistentes;
        this.detallesReserva= detallesReserva;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public UUID getIdPersona() { return idPersona;
    }

    public TipoUso getTipoDeUso() {
        return tipoDeUso;
    }

    public int getNumAsistentes(){
        return numAsistentes;
    }

    public String getDetallesReserva() {
        return detallesReserva;
    }

    /*public boolean estaDentroDeHorario(HorarioDisponible horario) {
        LocalDate fecha = this.fechaInicio;
        while (fecha.isBefore(this.fechaFin)) {
            if (fecha.getDayOfWeek() == horario.getDiaSemana() && horaInicio.isAfter(horario.getHoraInicio()) && horaFin.isBefore(horario.getHoraFin())) {
                return true;
            }
            fecha = fecha.plusDays(1);
        }
        return false;
    }*/
}
