package com.clickada.back.application;

import com.clickada.back.domain.EdificioRepository;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import com.clickada.back.infrastructure.EnviaMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;

@Service
public class EspacioService {
    EspacioRepository espacioRepository;
    PersonaRepository personaRepository;
    ReservaRepository  reservaRepository;
    EdificioRepository edificioRepository;
    EnviaMail servicioCorreo;

    @Autowired
    public EspacioService(EspacioRepository espacioRepository, PersonaRepository personaRepository,
                          ReservaRepository reservaRepository,EdificioRepository edificioRepository,
                          EnviaMail servicioCorreo){
        this.espacioRepository = espacioRepository;
        this.personaRepository = personaRepository;
        this.reservaRepository = reservaRepository;
        this.edificioRepository = edificioRepository;
        this.servicioCorreo = servicioCorreo;
    }
    @Transactional (readOnly = true)
    public List<Espacio> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    @Transactional
    public void cambiarReservabilidadEspacio(UUID idEspacio, Reservabilidad reservabilidad, UUID idPersona) throws Exception {
        Espacio espacio = espacioRepository.getById(idEspacio);
        Persona persona = personaRepository.getById(idPersona);
        if (espacio==null || persona==null) {
            throw new Exception("El espacio o la persona no existen");
        }
        espacio.modificarReservabilidad(persona,reservabilidad);
        espacioRepository.save(espacio);
    }

    @Transactional
    public Reserva reservarEspacio(Persona persona, List<Reserva> reservasTodas,Reserva reserva) throws Exception {
        //ver si esta dispponible reservar el espacio ese dia y esas horas
        List<Reserva> reservaList = new ArrayList<>();
        int totalAsistentesPermitidos = 0;
        if(persona!= null){ // Comprueba permisos de ese rol
            for(UUID idEspacio: reserva.getIdEspacios()){
                Espacio espacio = espacioRepository.getById(idEspacio);
                if(espacio == null) throw new Exception("El espacio que se quiere reservar no existe");
                if(espacio.getReservabilidad() == null) throw new Exception("El espacio que se quiere reservar no tiene reservabilidad");
                espacio.aptoParaReservar(persona);
                totalAsistentesPermitidos+=espacio.getTotalAsistentesPermitidos();
                List<Reserva> contienenEspacio = reservasTodas.stream()
                        .filter(reserva1 -> reserva1.getIdEspacios().stream()
                                .anyMatch(reserva.getIdEspacios()::contains))
                        .toList();
                reservaList.addAll(contienenEspacio); //añadimos reservas que tienen los mismo espacios (falta que sea la misma fecha)
            }
        }
        if(!reservaCorrecta(reservaList,reserva)){
            throw new Exception("Ya existe una reserva en el horario introducido");
        }
        if(totalAsistentesPermitidos<reserva.getNumOcupantes()){
            throw new Exception("Se supera el numero máximo de asistentes de los espacios seleccionados siendo "+
                    totalAsistentesPermitidos + " el total de asistentes permitidos y "+reserva.getNumOcupantes()
                    +" el numero de asistentes de la reserva.");
        }
        return reserva;
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
    @Transactional
    public void eliminarTodos() {
        this.espacioRepository.deleteAll();
        this.personaRepository.deleteAll();
    }
    @Transactional
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
    @Transactional
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
    @Transactional
    public void cambiarPorcentajeEdificio(UUID idPersona,double porcentajeNuevo) throws Exception{
        //Cambiar todos los espacios
        //tener de vuelta los espacios a los que ha afectado
        Persona persona = this.personaRepository.getById(idPersona);
        if(persona==null) throw new Exception("La persona no existe");
        Edificio edificio = getUnicoEdificio();
        double porcentajeViejo = edificio.getPorcentajeUsoPermitido();
        edificio.cambiarPorcentajeEdificio(porcentajeNuevo);
        edificioRepository.save(edificio);

        List<Espacio> todosEspacios = todosEspacios();
        List<UUID> espaciosAfectados = new ArrayList<>();
        for(Espacio espacio: todosEspacios){
            if(espacio.getPorcentajeUsoPermitido()==porcentajeViejo){ //le afecta
                espaciosAfectados.add(espacio.getIdEspacio());
            }
            espacio.modificarPorcentajeOcupacion(persona,porcentajeNuevo);
        }
        //Comprobamos las reservasVivas que tienen esos espacios asignados
        comprobarReservasVivas(espaciosAfectados);
        //notificar las reservas que con el cambio no han podido cumplir los requisitos
    }
    private Edificio getUnicoEdificio(){
        return edificioRepository.findAll().get(0);
    }
    @Transactional
    public void cambiarPorcentajeEspacio(UUID idPersona, UUID idEspacio,double porcentajeNuevo) throws Exception{
        Persona persona = this.personaRepository.getById(idPersona);
        if(persona==null) throw new Exception("La persona no existe");
        //hacer el cambio
        Espacio espacio = espacioRepository.getById(idEspacio);
        if(espacio==null){
            throw new Exception("No existe el espacio con ese id");
        }
        espacio.modificarPorcentajeOcupacion(persona,porcentajeNuevo);
        espacioRepository.save(espacio);
        //Comprobamos las reservasVivas que tienen ese espacio asignado
        // se notifica las reservas que con el cambio no han podido cumplir los requisitos
        comprobarReservasVivas(List.of(idEspacio));
    }
    /*
    * Dodo una lista de espacios, comprueba las reservasVivas que tengan asignado ese espacio
    * y devuelve la lista de reservas que no cumplen los requisitos
    * */
    private void comprobarReservasVivas(List<UUID> idEspacios) throws Exception{
        List<Reserva> reservasVivas = new java.util.ArrayList<>(reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getFecha().isAfter(LocalDate.now())).toList());
        reservasVivas.addAll(reservaRepository.findByFecha(LocalDate.now())
                .stream().filter(reserva1 -> reserva1.getPeriodoReserva().getHoraFin().isAfter(LocalTime.now()))
                .toList());
        reservasVivas.stream().filter(reserva1 -> reserva1.getIdEspacios().stream()
                        .anyMatch(idEspacios::contains))
                .toList();
        int totalAsistentesPermitidos;

        for(Reserva reserva  : reservasVivas){
            totalAsistentesPermitidos = 0;
            for(UUID idEspacio : reserva.getIdEspacios()){
                totalAsistentesPermitidos+=espacioRepository.getById(idEspacio).getTotalAsistentesPermitidos();
            }
            if(totalAsistentesPermitidos<reserva.getNumOcupantes()){
                String mail = this.personaRepository.getById(reserva.getIdPersona()).getEMail();

                Executors.newSingleThreadExecutor()
                        .execute(() -> servicioCorreo.enviarCorreo(mail,1,"nombre",reserva.getFecha(),
                                reserva.getPeriodoReserva().getHoraInicio()));

                reservaRepository.delete(reserva);
            }
        }
    }
}
