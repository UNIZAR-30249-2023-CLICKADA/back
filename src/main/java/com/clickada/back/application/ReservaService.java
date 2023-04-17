package com.clickada.back.application;

import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.PeriodoReserva;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {
    ReservaRepository reservaRepository;
    PersonaRepository personaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, PersonaRepository personaRepository){
        this.reservaRepository = reservaRepository;
        this.personaRepository = personaRepository;
    }

    public List<Reserva> listarTodasReservas(){
        return reservaRepository.findAll();
    }
    public List<Reserva> obtenerReservasVivas(UUID idPersona) throws Exception {
        if(!personaRepository.existsById(idPersona)) throw new Exception("Esa persona no existe");

        Persona persona = personaRepository.getById(idPersona);
        if(!persona.rolPrincipal().equals(Rol.GERENTE)) {
            throw new Exception("Necesitas rol de gerente para obtener reservas vivas");
        }
        List<Reserva> resultado = new java.util.ArrayList<>(reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getFecha().isAfter(LocalDate.now())).toList());
        resultado.addAll(reservaRepository.findByFecha(LocalDate.now())
                .stream().filter(reserva1 -> reserva1.getPeriodoReserva().getHoraFin().isAfter(LocalTime.now()))
                .toList());
        return resultado;
    }
    public void eliminarTodas() {
        this.reservaRepository.deleteAll();
    }
}
