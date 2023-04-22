package com.clickada.back.webtier.dtos;

import lombok.Getter;

import java.util.UUID;

@Getter

public class ReservaAutomaticaDto {
    UUID idPersona;
    int numEspacios;
    int numMaxPersonas;
    String fecha;
    String horaInicio;
    String horaFinal;
    String detalles;
    String tipoUso;
}
