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
import java.util.*;

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

    public boolean cambiarReservabilidadEspacio(UUID idEspacio, Reservabilidad reservabilidad, UUID idPersona) throws Exception {
        if (espacioRepository.existsById(idEspacio) && personaRepository.existsById(idPersona)) {
            Espacio espacio = espacioRepository.getById(idEspacio);
            Persona persona = personaRepository.getById(idPersona);
            espacio.modificarReservabilidad(persona,reservabilidad);
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
        int totalAsistentesPermitidos = 0;
        if(persona!= null){ // Comprueba permisos de ese rol
            for(UUID idEspacio: idEspacios){
                Espacio espacio = espacioRepository.getById(idEspacio);
                if(espacio != null && !espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN) &&
                        espacio.getReservabilidad() !=null &&
                !espacio.getReservabilidad().categoriaReserva.equals(CategoriaReserva.SALA_COMUN) &&
                        persona.rolPrincipal().equals(Rol.ESTUDIANTE)){
                    throw new Exception("Un estudiante solo puede reservar SALAS COMUNES");
                }
                if(espacio != null && espacio.getCategoriaEspacio().equals(CategoriaEspacio.AULA) &&
                        espacio.getReservabilidad() !=null &&
                        espacio.getReservabilidad().categoriaReserva.equals(CategoriaReserva.AULA) &&
                        persona.rolPrincipal().equals(Rol.TECNICO_LABORATORIO)){
                    throw new Exception("Un Tecnico de laboratorio no puede reservar aulas");
                }
                if(espacio != null && espacio.getCategoriaEspacio().equals(CategoriaEspacio.LABORATORIO) &&
                        espacio.getReservabilidad() !=null &&
                        espacio.getReservabilidad().categoriaReserva.equals(CategoriaReserva.LABORATORIO) &&
                        (persona.rolPrincipal().equals(Rol.TECNICO_LABORATORIO) ||
                            persona.rolPrincipal().equals(Rol.INVESTIGADOR_CONTRATADO) ||
                            persona.rolPrincipal().equals(Rol.DOCENTE_INVESTIGADOR)
                        )){
                    if(!espacio.getPropietarioEspacio().esDepartamento()){
                        throw new Exception("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                                "laboratiorios que esten adscritos a un departamento");
                    }
                    if(!espacio.getPropietarioEspacio().departamento.equals(persona.getAdscripcion().departamento)){
                        throw new Exception("Los tecnicos de laboratorio, investigador contratado y docente investigador solo pueden reservar " +
                                "laboratorios de su mismo departamento");
                    }

                }
                if(espacio.getReservabilidad()!=null && !espacio.getReservabilidad().reservable){
                    throw new Exception("El espacio "+ espacio.getIdEspacio()+ " no es reservable. " +
                            "Espere a que un gerente lo habilite");
                }
                totalAsistentesPermitidos+=espacio.getTotalAsistentesPermitidos();
                List<Reserva> contienenEspacio = reservasTodas.stream()
                        .filter(reserva1 -> reserva1.getIdEspacios().stream()
                                .anyMatch(idEspacios::contains))
                        .toList();
                reservaList.addAll(contienenEspacio); //añadimos reservas que tienen los mismo espacios (falta que sea la misma fecha)
            }
        }
        if(!reservaCorrecta(reservaList,reserva)){
            throw new Exception("Ya existe una reserva en el horario introducido");
        }
        if(totalAsistentesPermitidos<numAsistentes){
            throw new Exception("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                    totalAsistentesPermitidos + " el total de asistentes permitidos y "+numAsistentes
                    +" el numero de asistentes de la reserva.");
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

    public void eliminarTodos() {
        this.espacioRepository.deleteAll();
        this.personaRepository.deleteAll();
    }
    public List<UUID> buscarEspacios(UUID idPersona, int numEspacios, LocalDate fecha, LocalTime horaInicio, LocalTime horaFinal, int numMaxPersonas,TipoUso tipoDeUso, String detalles) throws Exception {
        if(numEspacios>3){
            throw new Exception("Demasiados espacios para la reserva automatica");
        }
        Persona persona = personaRepository.getById(idPersona);
        List<Reserva> reservasTodas = reservaRepository.findByFecha(fecha);
        List<Espacio> listaEspacios = espacioRepository.findAll();
        List<UUID> espaciosFiltrados = new ArrayList<>();
        int totalAsistentesPermitidos = 0;
        if(persona!= null){ // Comprueba permisos de ese rol
            for(Espacio espacio: listaEspacios) {
                if ((espacio != null && espacio.getReservabilidad() != null) && (
                        (espacio.getCategoriaEspacio().equals(CategoriaEspacio.SALA_COMUN) &&
                                espacio.getReservabilidad().categoriaReserva.equals(CategoriaReserva.SALA_COMUN) &&
                                persona.rolPrincipal().equals(Rol.ESTUDIANTE)) || !persona.rolPrincipal().equals(Rol.ESTUDIANTE))
                        && espacio.getReservabilidad().reservable) {
                espaciosFiltrados.add(espacio.getIdEspacio());
            }
            }
        }
        List<Reserva> contienenEspacio = reservasTodas.stream()
                .filter(reserva1 -> reserva1.getIdEspacios().stream().anyMatch(espaciosFiltrados::contains))
                .toList();
        List<Reserva> reservaList = new ArrayList<>();
        reservaList.addAll(contienenEspacio); //añadimos reservas que tienen los mismo espacios (falta que sea la misma fecha)
        List<Espacio> espaciosCorrectos = espaciosDisponibles(reservaList,espaciosFiltrados,new PeriodoReserva(horaInicio,horaFinal));
        ArrayList<UUID> listaAdevolver = new ArrayList<>();
        List<Integer> listaMaxOcupantes = new ArrayList<>();

        if (espaciosCorrectos.size() < numEspacios) {
            throw new Exception("No existen esapcios suficientes disponibles con esas caracteristicas");
        }
        Comparator<Espacio> comparador = new Comparator<Espacio>() {
            @Override
            public int compare(Espacio num1, Espacio num2) {
                Integer n1 = num1.getNumMaxOcupantes();
                Integer n2 = num2.getNumMaxOcupantes();
                return n2.compareTo(n1);
            }
        };

        Collections.sort(espaciosCorrectos, comparador);

        int suma = 0;
        for (int i = 0; i < numEspacios; i++) {
            suma += espaciosCorrectos.get(i).getNumMaxOcupantes();
            listaAdevolver.add(espaciosCorrectos.get(i).getIdEspacio());
        }
        if(suma<numMaxPersonas){
            throw new Exception("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan");
        }
        for (int i = 0; i < numEspacios; i++) {
            reservaRepository.save(new Reserva((new PeriodoReserva(horaInicio,horaFinal)),idPersona,
                    tipoDeUso, listaAdevolver, numMaxPersonas, detalles,fecha));
        }

        return listaAdevolver;
    }
    private List<Espacio> espaciosDisponibles(List<Reserva> reservaList,List<UUID> espacioList, PeriodoReserva periodoReserva){
        List<Espacio> espacioListDisponible = new ArrayList<>();
        if(reservaList!=null) {
            for (UUID idEspacio : espacioList) {
                espacioListDisponible.add(espacioRepository.getById(idEspacio));
                for (Reserva reserva : reservaList) {
                    if (reserva.getIdEspacios().contains(idEspacio)) {//si esteidEspacio espacio esta reservado ese mismo dia tambien
                        if (!reserva.getPeriodoReserva().periodosCompatibles(periodoReserva)) {
                            espacioListDisponible.remove(espacioList.indexOf(espacioRepository.getById(idEspacio)));
                        }
                    }
                }
            }
        }else{
            for (UUID idEspacio : espacioList) {
                espacioList.add(idEspacio);
            }
        }
        return espacioListDisponible;
    }
}
