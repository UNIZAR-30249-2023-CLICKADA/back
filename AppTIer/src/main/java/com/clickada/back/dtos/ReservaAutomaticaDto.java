package com.clickada.back.dtos;

import com.clickada.back.domain.entity.auxClasses.TipoUso;
import lombok.Getter;

import java.util.ArrayList;
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
    TipoUso tipoUso;
}
