package com.clickada.back.application;

import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EspacioService {
    EspacioRepository espacioRepository;
    PersonaRepository personaRepository;
    ReservaRepository  reservaRepository;

    @Autowired
    public EspacioService(EspacioRepository espacioRepository, PersonaRepository personaRepository,
                          ReservaRepository reservaRepository){
        this.espacioRepository = espacioRepository;
        this.personaRepository = personaRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Espacio> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    public boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable) {
        if (espacioRepository.existsById(idEspacio)) {
            Espacio espacio = espacioRepository.getById(idEspacio);
            Reservabilidad reservabilidad = espacio.getReservabilidad();
            Reservabilidad reservabilidadNueva = new Reservabilidad(reservable, reservabilidad.categoriaReserva);
            espacio.setReservabilidad(reservabilidadNueva);
            espacioRepository.save(espacio);
            return true;
        }
        return false;
    }

    public boolean reservarEspacio(UUID idPersona, ArrayList<UUID> idEspacios, LocalDate fecha, LocalTime horaInicio,
                                   LocalTime horaFinal, TipoUso uso,int numAsistentes,String detalles) throws Exception {
        //Habrá que controlar todas las restricciones
        Reserva reserva = new Reserva(new PeriodoReserva(horaInicio,horaFinal),idPersona,uso,idEspacios,numAsistentes,detalles,fecha);
        Persona persona = personaRepository.getById(idPersona);
        //ver si esta dispponible reservar el espacio ese dia y esas horas
        List<Reserva> reservaList = new ArrayList<>();
        List<Reserva> reservasTodas = reservaRepository.findByFecha(fecha);
        if(persona!= null){ // Comprueba permisos de ese rol
            if(persona.rolPrincipal().equals(Rol.ESTUDIANTE)){
                for(UUID idEspacio: idEspacios){
                    Espacio espacio = espacioRepository.getById(idEspacio);
                    if(espacio != null && !espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)){
                        throw new Exception("Un estudiante solo puede reservar SALAS COMUNES");
                    }
                    List<Reserva> contienenEspacio = reservasTodas.stream()
                            .filter(reserva1 -> reserva1.getIdEspacios().stream()
                                    .anyMatch(idEspacios::contains))
                            .toList();
                    reservaList.addAll(contienenEspacio); //añadimos reservas que tienen los mismo espacios (falta que sea la misma fecha)
                }
            }
        }
        if(!reservaCorrecta(reservaList,reserva)){
            throw new Exception("Ya existe una reserva en el horario introducido");
        }
        reservaRepository.save(reserva);
        return true;
    }

    private boolean reservaCorrecta(List<Reserva> reservaList,Reserva reservaNueva){
        for(UUID idEspacio: reservaNueva.getIdEspacios()){
            for(Reserva reserva: reservaList){
                if(reserva.getIdEspacios().contains(idEspacio)){//si este espacio esta reservado ese mismo dia tambien
                    if(!reserva.getPeriodoReserva().periodosCompatibles(reservaNueva.getPeriodoReserva())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Reserva> obtenerReservasVivas(UUID idPersona) {
        List<Reserva> l = new ArrayList<>();
        if(this.personaRepository.existsById(idPersona) &&
                this.personaRepository.getById(idPersona).rolPrincipal().equals(Rol.GERENTE)){
            //l.addAll(this.reservaRepository.findAllAfterTime()) ;
        }
        return l;
    }
}
