package com.clickada.back.application;

import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DominioService {
    @Autowired
    ReservaService reservaService;
    @Autowired
    EspacioService espacioService;
    @Autowired
    public PersonaService personaService;

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
    public void eliminarReserva(UUID idPersona, UUID idReserva) throws Exception {
        if(!personaService.aptoParaCambiar(idPersona)) {
            throw new Exception("Se necesita un rol gerente para eliminar una reserva");
        }
        Persona gerente = personaService.getPersonaById(idPersona);
        reservaService.eliminarReserva(idReserva,gerente);
    }
    @Transactional
    public ArrayList<UUID> reservaAutomaticaEspacio(UUID idPersona, int numEspacios, LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal, int numMaxPersonas, TipoUso tipoDeUso, String detalles) throws Exception {
        if(numEspacios>3){
            throw new Exception("Demasiados espacios para la reserva automatica");
        }
        Persona persona = personaService.getPersonaById(idPersona);
        List<Reserva> reservasTodas = reservaService.reservasPorFecha(fecha);
        ArrayList<UUID> listaEspacios = espacioService.buscarEspacios(persona,reservasTodas,numEspacios,horaInicio,horaFinal,numMaxPersonas);
        Reserva reserva = new Reserva((new PeriodoReserva(horaInicio,horaFinal)),idPersona,
                tipoDeUso, listaEspacios, numMaxPersonas, detalles,fecha);

        reservaService.reservar(reserva);
        return listaEspacios;

    }
    @Transactional
    public void cambiarRol(UUID idGerente,UUID idPersona, String rol, String departamentoString) throws Exception {
        if(!personaService.aptoParaCambiar(idGerente)) {
            throw new Exception("Se ncesita un rol gerente para cambiar el rol de cualquier persona");
        }
        Persona gerente = personaService.getPersonaById(idGerente);
        Persona persona_antigua = personaService.getPersonaById(idPersona);
        Departamento departamentoAntiguo = persona_antigua.getDepartamento();
        boolean asignable = persona_antigua.asignable();
        Persona persona = personaService.cambiarRol(idPersona, rol, departamentoString);
        List<Reserva> reservasVivasPersona = reservaService.reservasVivasPersona(gerente,persona);
        List<Espacio> espaciosList = espacioService.obtenerEspaciosReservas(reservasVivasPersona);
        reservaService.comprobarReservas(persona,reservasVivasPersona,espaciosList);
        //comprobar Despachos (lo unico que le puede afectar un cambio de rol)
        espacioService.comprobarDespachos(asignable,departamentoAntiguo,persona);
    }
    @Transactional
    public void cambiarReservabilidadEspacio(UUID idEspacio, Reservabilidad reservabilidad, UUID idPersona) throws Exception {
        Persona persona = personaService.getPersonaById(idPersona);
        personaService.aptoParaCambiar(idPersona);
        espacioService.cambiarReservabilidadEspacio(idEspacio,reservabilidad,persona);
        List<Reserva> reservasVivasEspacios = reservaService.reservasVivasEspacios(List.of(idEspacio));
        comprobarReservas(reservasVivasEspacios);
    }
    @Transactional
    public void cambiarPropietarioEspacio(UUID idEspacio, PropietarioEspacio propietarioEspacio, UUID idPersona) throws Exception{
        if(!personaService.aptoParaCambiar(idPersona)) {
            throw new Exception("Se ncesita un rol gerente para cambiar el rol de cualquier persona");
        }
        if(propietarioEspacio.esPersonas()){
            personaService.comprobarPropietarios(propietarioEspacio);
        }
        espacioService.cambiarPropietarioEspacio(idEspacio,propietarioEspacio);
    }
    @Transactional
    public void cambiarPorcentajeEspacio(UUID idPersona, UUID idEspacio,double porcentajeNuevo) throws Exception{
        Persona persona = personaService.getPersonaById(idPersona);
        espacioService.cambiarPorcentajeEspacio(persona,idEspacio,porcentajeNuevo);
        List<Reserva> reservasVivasEspacios = reservaService.reservasVivasEspacios(List.of(idEspacio));
        comprobarReservas(reservasVivasEspacios);
    }
    @Transactional
    public void cambiarPorcentajeEdificio(UUID idPersona,double porcentajeNuevo) throws Exception{
        Persona persona = personaService.getPersonaById(idPersona);
        List<UUID> espaciosAfectados = espacioService.cambiarPorcentajeEdificio(persona,porcentajeNuevo);
        List<Reserva> reservasVivasEspacios = reservaService.reservasVivasEspacios(espaciosAfectados);
        comprobarReservas(reservasVivasEspacios);
    }
    private void comprobarReservas(List<Reserva> reservasVivasEspacios) throws Exception {
        if(reservasVivasEspacios.size()>0){
            List<UUID> listIdPersona = new ArrayList<>();
            reservasVivasEspacios.forEach(reserva -> listIdPersona.add(reserva.getIdPersona()));

            List<Persona> personasImplicadas = personaService.getPersonasById(listIdPersona);

            List<Espacio> espaciosImplicados = espacioService.obtenerEspaciosReservas(reservasVivasEspacios);

            reservaService.comprobarReservasEspacios(reservasVivasEspacios,espaciosImplicados,personasImplicadas);
        }
    }
}
