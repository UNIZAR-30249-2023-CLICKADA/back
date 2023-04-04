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
public class EspacioReservableServiceImpl implements EspacioReservableService{
    private final EspacioRepository espacioRepository;
    private final PersonaRepository personaRepository;
    private final ReservaRepository  reservaRepository;

    @Autowired
    public EspacioReservableServiceImpl(EspacioRepository espacioRepository, PersonaRepository personaRepository,
                                        ReservaRepository reservaRepository){
        this.espacioRepository = espacioRepository;
        this.personaRepository = personaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<Espacio> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    @Override
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
    @Override
    public boolean reservarEspacio(UUID idPersona, List<UUID> idEspacios, LocalDate fecha, LocalTime horaInicio,
                                   LocalTime horaFinal, TipoUso uso,int numAsistentes,String detalles) {
        //Habrá que controlar todas las restricciones
        Reserva r = new Reserva(new PeridodoReserva(fecha,horaInicio,horaFinal),idPersona,uso,idEspacios,numAsistentes,detalles);
        Persona persona = personaRepository.getById(idPersona);
        if(persona!= null){
            if(persona.getRoles().get(0).equals(Rol.ESTUDIANTE)){
                for(UUID idEspacio: idEspacios){
                    Espacio espacio = espacioRepository.getById(idEspacio);
                    if(espacio != null && !espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN)){
                        return false;
                    }
                }
            }
        }
        reservaRepository.save(r);
        return true;
    }

    @Override
    public List<Reserva> obtenerReservasVivas(UUID idPersona) {
        List<Reserva> l = new ArrayList<>();
        if(this.personaRepository.existsById(idPersona) &&
                this.personaRepository.getById(idPersona).getRoles().get(0).equals(Rol.GERENTE)){
            l.addAll(this.reservaRepository.findAllAfterTime()) ;
        }
        return l;
    }
}
