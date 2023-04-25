package com.clickada.back.application;

import com.clickada.back.domain.EdificioRepository;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.PeriodoReserva;
import com.clickada.back.domain.entity.auxClasses.TipoUso;
import com.clickada.back.infrastructure.EnviaMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DominioService {
    @Autowired
    ReservaService reservaService;
    @Autowired
    EspacioService espacioService;
    @Autowired
    PersonaService personaService;

    @Autowired
    public DominioService(ReservaService reservaService,EspacioService espacioService,PersonaService personaService){
        this.reservaService = reservaService;
        this.espacioService = espacioService;
        this.personaService = personaService;
    }
    @Transactional
    public void reservarEspacio(UUID idPersona, ArrayList<UUID> idEspacios, LocalDate fecha, LocalTime horaInicio,
                                LocalTime horaFinal, TipoUso uso, int numAsistentes, String detalles) throws Exception {
        Persona persona = personaService.getPersonaById(idPersona);
        List<Reserva> reservasTodas = reservaService.reservasPorFecha(fecha);
        Reserva reserva = new Reserva(new PeriodoReserva(horaInicio,horaFinal),persona.getIdPersona(),uso,idEspacios,numAsistentes,detalles,fecha);
        Reserva reservaCompletada = espacioService.reservarEspacio(persona,reservasTodas,reserva);
        reservaService.reservar(reservaCompletada);
    }

    @Transactional
    public void cambiarRol(UUID idGerente,UUID idPersona, String rol, String departamentoString) throws Exception {
        if(personaService.aptoParaCambiar(idGerente)) {
            Persona gerente = personaService.getPersonaById(idPersona);
            Persona persona = personaService.cambiarRol(idPersona, rol, departamentoString);
            List<Reserva> reservasVivasPersona = reservaService.reservasVivasPersona(gerente,persona);
            List<Espacio> espaciosList = espacioService.obtenerEspaciosReservas(reservasVivasPersona);

            reservaService.comprobarReservas(persona,reservasVivasPersona,espaciosList);
        }
    }

}
