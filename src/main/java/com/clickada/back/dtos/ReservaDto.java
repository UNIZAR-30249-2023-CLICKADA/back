package com.clickada.back.dtos;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class ReservaDto {
    UUID idPersona;
    ArrayList<UUID> idEspacios;
    String stringTipoUso;
    int numMaxPersonas;
    LocalDate fecha;
    LocalTime horaInicio;
    LocalTime horaFinal;
    String detalles;
}
