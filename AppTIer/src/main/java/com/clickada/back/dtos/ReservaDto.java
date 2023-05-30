package com.clickada.back.dtos;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Builder
@Getter
public class ReservaDto {
    UUID idReserva;
    UUID idPersona;
    ArrayList<UUID> idEspacios;
    String stringTipoUso;
    int numMaxPersonas;
    String fecha;
    String horaInicio;
    String horaFinal;
    String detalles;
}
