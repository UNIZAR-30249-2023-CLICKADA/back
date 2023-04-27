package com.clickada.back.dtos;

import com.clickada.back.domain.entity.Espacio;

import java.util.ArrayList;
import java.util.List;

public class MapperDtos {
    public EspacioDto deEspacioAEspacioDto(Espacio espacio){
        return EspacioDto.builder()
                .idEspacio(espacio.getIdEspacio())
                .categoriaEspacio(espacio.getCategoriaEspacio())
                .tamanyo(espacio.getTamanyo())
                .reservabilidad(espacio.getReservabilidad())
                .numMaxOcupantes(espacio.getNumMaxOcupantes())
                .porcentajeUsoPermitido(espacio.getPorcentajeUsoPermitido())
                .horaInicio(espacio.getHoraInicio().toString())
                .horaFin(espacio.getHoraFin().toString())
                .propietarioEspacio(espacio.getPropietarioEspacio())
                .build();
    }
    public List<EspacioDto> listaEspacioDto(List<Espacio> espacios){
        List<EspacioDto> listaDto = new ArrayList<>();
        espacios.forEach(espacio -> listaDto.add(deEspacioAEspacioDto(espacio)));
        return listaDto;
    }
}
