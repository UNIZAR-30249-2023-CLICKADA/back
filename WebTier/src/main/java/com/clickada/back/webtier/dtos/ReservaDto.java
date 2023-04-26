package com.clickada.back.webtier.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class ReservaDto {
    UUID idPersona;
    ArrayList<UUID> idEspacios;
    String stringTipoUso;
    int numMaxPersonas;
    String fecha;
    String horaInicio;
    String horaFinal;
    String detalles;
}
