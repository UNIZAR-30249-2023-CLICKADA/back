package com.clickada.back.application;

import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.EspacioReservable;
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
    public List<EspacioReservable> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    @Override
    public boolean cambiarReservabilidadEspacio(UUID idEspacio, boolean reservable) {
        if (espacioRepository.existsById(idEspacio)) {
            EspacioReservable espacioReservable = espacioRepository.getById(idEspacio);
            Reservabilidad reservabilidad = espacioReservable.getReservabilidad();
            Reservabilidad reservabilidadNueva = new Reservabilidad(reservable, reservabilidad.categoriaReserva);
            espacioReservable.setReservabilidad(reservabilidadNueva);
            espacioRepository.save(espacioReservable);
            return true;
        }
        return false;
    }
    @Override
    public boolean reservarEspacio(UUID idPersona, UUID idEspacio, LocalDate fecha, LocalTime desde,
                                   LocalTime hasta, TipoUso uso,int numAsistentes,String detalles) {
        //Habrá que controlar todas las restricciones
        Reserva r = new Reserva(fecha,desde,fecha,hasta,idPersona,uso,idEspacio,numAsistentes,detalles);
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
