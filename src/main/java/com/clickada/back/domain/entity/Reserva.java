package com.clickada.back.domain.entity;

import com.clickada.back.domain.entity.auxClasses.PeriodoReserva;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reserva {
    @Id
    private UUID idReserva;
    @Transient
    private PeriodoReserva periodoReserva;
    private UUID idPersona;
    private TipoUso tipoDeUso;
    private int numOcupantes;
    private String detallesReserva;

    private ArrayList<UUID> idEspacios;


    public Reserva(PeriodoReserva periodoReserva, UUID idPersona,
                   TipoUso tipoDeUso, ArrayList<UUID> idEspacios, int numOcupantes, String detallesReserva) {
        this.idReserva = UUID.randomUUID();
        this.idEspacios = idEspacios;
        this.periodoReserva = periodoReserva;
        this.idPersona = idPersona;
        this.tipoDeUso = tipoDeUso;
        this.numOcupantes = numOcupantes;
        this.detallesReserva = detallesReserva;
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
