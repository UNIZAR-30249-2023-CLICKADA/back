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
import java.time.LocalDate;
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
    private PeriodoReserva periodoReserva;
    private UUID idPersona;
    private TipoUso tipoDeUso;
    private int numOcupantes;
    private String detallesReserva;
    private LocalDate fecha;
    private ArrayList<UUID> idEspacios;


    public Reserva(PeriodoReserva periodoReserva, UUID idPersona,
                   TipoUso tipoDeUso, ArrayList<UUID> idEspacios, int numOcupantes, String detallesReserva,LocalDate fecha) {
        this.idReserva = UUID.randomUUID();
        this.idEspacios = idEspacios;
        this.periodoReserva = periodoReserva;
        this.idPersona = idPersona;
        this.tipoDeUso = tipoDeUso;
        this.numOcupantes = numOcupantes;
        this.detallesReserva = detallesReserva;
        this.fecha = fecha;
    }

}
