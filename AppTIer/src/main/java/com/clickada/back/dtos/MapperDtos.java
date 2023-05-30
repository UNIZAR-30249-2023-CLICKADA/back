package com.clickada.back.dtos;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;

import java.util.ArrayList;
import java.util.List;

public class MapperDtos {
    public EspacioDto deEspacioAEspacioDto(Espacio espacio){
        return EspacioDto.builder()
                .idEspacio(espacio.getIdEspacio())
                .categoriaEspacio(espacio.getCategoriaEspacio())
                .tamanyo(espacio.getTamanyo())
                .nombre(espacio.getNombre())
                .reservabilidad(espacio.getReservabilidad())
                .numMaxOcupantes(espacio.getNumMaxOcupantes())
                .porcentajeUsoPermitido(espacio.getPorcentajeUsoPermitido())
                .horaInicio(espacio.getHoraInicio().toString())
                .horaFin(espacio.getHoraFin().toString())
                .propietarioEspacio(espacio.getPropietarioEspacio())
                .build();
    }

    public ReservaDto deReservaAReservaDto(Reserva reserva){
        return ReservaDto.builder()
                .idReserva(reserva.getIdReserva())
                .idPersona(reserva.getIdPersona())
                .idEspacios(reserva.getIdEspacios())
                .stringTipoUso(reserva.getTipoDeUso().toString())
                .numMaxPersonas(reserva.getNumOcupantes())
                .fecha(reserva.getFecha().toString())
                .horaInicio(reserva.getPeriodoReserva().getHoraInicio().toString())
                .horaFinal(reserva.getPeriodoReserva().getHoraFin().toString())
                .detalles(reserva.getDetallesReserva())
                .build();
    }

    public PersonaDto dePersonaAPersonaDto(Persona persona){
        return PersonaDto.builder()
                .idPersona(persona.getIdPersona())
                .nombre(persona.getNombre())
                .eMail(persona.getEMail())
                .departamento(persona.getDepartamento())
                .departamentoDisponible(persona.isDepartamentoDisponible())
                .roles(persona.getRoles())
                .build();
    }
    public List<ReservaDto> listaReservaDto(List<Reserva> reservas){
        List<ReservaDto> listaDto = new ArrayList<>();
        reservas.forEach(reserva -> listaDto.add(deReservaAReservaDto(reserva)));
        return listaDto;
    }

    public List<PersonaDto> listaPersonaDto(List<Persona> personas){
        List<PersonaDto> listaDto = new ArrayList<>();
        personas.forEach(persona -> listaDto.add(dePersonaAPersonaDto(persona)));
        return listaDto;
    }

    public List<EspacioDto> listaEspacioDto(List<Espacio> espacios){
        List<EspacioDto> listaDto = new ArrayList<>();
        espacios.forEach(espacio -> listaDto.add(deEspacioAEspacioDto(espacio)));
        return listaDto;
    }
}
