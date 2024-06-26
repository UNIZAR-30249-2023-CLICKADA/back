package com.clickada.back.application;

import com.clickada.back.domain.EdificioRepository;
import com.clickada.back.domain.EspacioRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
@Slf4j
@Service
public class EspacioService {
    EspacioRepository espacioRepository;
    EdificioRepository edificioRepository;
    EnviaMail servicioCorreo;

    @Autowired
    public EspacioService(EspacioRepository espacioRepository,
                          EdificioRepository edificioRepository,
                          EnviaMail servicioCorreo){
        this.espacioRepository = espacioRepository;
        this.edificioRepository = edificioRepository;
        this.servicioCorreo = servicioCorreo;
    }
    @Transactional (readOnly = true)
    public List<Espacio> todosEspacios() {
        return this.espacioRepository.findAll();
    }

    @Transactional
    public void cambiarReservabilidadEspacio(UUID idEspacio, Reservabilidad reservabilidad, Persona persona) throws Exception {
        Espacio espacio = espacioRepository.getById(idEspacio);
        if (espacio==null) {
            throw new Exception("El espacio no existe");
        }
        Reservabilidad reservabilidad_antigua = espacio.getReservabilidad();
        espacio.modificarReservabilidad(persona,reservabilidad);
        espacioRepository.save(espacio);
    }
    public void comprobarDiaNoReservable(LocalDate fecha) throws Exception {
        Edificio edificio = getUnicoEdificio();
        if(edificio.getDiasNoReservables().contains(fecha)){
            throw new Exception("El edificio no tiene habilitado ese día como reservable");
        }
    }
    @Transactional
    public Reserva reservarEspacio(Persona persona, List<Reserva> reservasTodas,Reserva reserva) throws Exception {
        //ver si esta dispponible reservar el espacio ese dia y esas horas
        comprobarDiaNoReservable(reserva.getFecha());
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
    public ArrayList<UUID> buscarEspacios(Persona persona,List<Reserva> reservasTodas, int numEspacios, LocalTime horaInicio, LocalTime horaFinal, int numMaxPersonas) throws Exception {
        if(numEspacios>3){
            throw new Exception("Demasiados espacios para la reserva automatica");
        }
        List<Espacio> listaEspacios = espacioRepository.findAll();
        List<UUID> espaciosFiltrados = new ArrayList<>();
        //Se añaden los espacios que son reservables y que la persona puede reservar por su Rol
        for(Espacio espacio: listaEspacios) {
            if ((espacio != null && espacio.getReservabilidad() != null) && (espacio.aptoCambioRol_Y_Reservabilidad(persona))){
                espaciosFiltrados.add(espacio.getIdEspacio());
            }
        }
        //Espacios que tienen reserva el mismo dia
        List<Reserva> contienenEspacio = reservasTodas.stream()
                .filter(reserva1 -> reserva1.getIdEspacios().stream().anyMatch(espaciosFiltrados::contains))
                .toList();
        List<Reserva> reservaList = new ArrayList<>();
        reservaList.addAll(contienenEspacio); //añadimos reservas que tienen los mismo espacios (falta que sea la misma fecha)
        List<Espacio> espaciosCorrectos = espaciosDisponibles(reservaList,espaciosFiltrados,new PeriodoReserva(horaInicio,horaFinal));
        ArrayList<UUID> listaAdevolver = new ArrayList<>();

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
        List<Espacio> espaciosCorrectos2 = new ArrayList<>();
        espaciosCorrectos2.addAll(espaciosCorrectos);
        espaciosCorrectos2.sort(comparador);
        int suma = 0;
        for (int i = 0; i < numEspacios; i++) {
            suma += espaciosCorrectos2.get(i).getNumMaxOcupantes();
            listaAdevolver.add(espaciosCorrectos2.get(i).getIdEspacio());
        }
        if(suma<numMaxPersonas){
            throw new Exception("La cantidad de personas para la reserva es demasiado grande para la cantidad de espacios que se proporcionan");
        }

        return listaAdevolver;
    }
    @Transactional
    private List<Espacio> espaciosDisponibles(List<Reserva> reservaList,List<UUID> espacioList, PeriodoReserva periodoReserva){
        List<Espacio> espacioListDisponible = new ArrayList<>();
        List<UUID> idDisponibles = new ArrayList<>(espacioList.stream().toList());
        if(reservaList!=null && reservaList.size()>0) {
            for (UUID idEspacio : espacioList) {
                for (Reserva reserva : reservaList) {
                    if (reserva.getIdEspacios().contains(idEspacio)) {//si este idEspacio espacio esta reservado ese mismo dia tambien
                        if (!reserva.getPeriodoReserva().periodosCompatibles(periodoReserva)) { // si no son periodos compatibles para esta reserva se elimina de los
                            idDisponibles.remove(idEspacio);
                        }
                    }
                }
            }
            espacioListDisponible = espacioRepository.findAllById(idDisponibles);
        }else{
            espacioListDisponible = espacioRepository.findAllById(espacioList);
        }
        return espacioListDisponible;
    }
    @Transactional
    public List<UUID> cambiarPorcentajeEdificio(Persona persona, double porcentajeNuevo) throws Exception{
        //Cambiar todos los espacios
        //tener de vuelta los espacios a los que ha afectado
        Edificio edificio = getUnicoEdificio();
        double porcentajeViejo = edificio.getPorcentajeUsoPermitido();
        edificio.cambiarPorcentajeEdificio(porcentajeNuevo);
        edificioRepository.save(edificio);

        List<Espacio> todosEspacios = todosEspacios();
        List<UUID> espaciosAfectados = new ArrayList<>();
        for(Espacio espacio: todosEspacios){
            if(espacio.getPorcentajeUsoPermitido()==porcentajeViejo){ //le afecta
                espaciosAfectados.add(espacio.getIdEspacio());
                espacio.modificarPorcentajeOcupacion(persona,porcentajeNuevo);
            }
        }
        //Comprobamos las reservasVivas que tienen esos espacios asignados
        //comprobarReservasVivas(espaciosAfectados);
        //notificar las reservas que con el cambio no han podido cumplir los requisitos
        return espaciosAfectados;
    }
    public Edificio getUnicoEdificio(){
        return edificioRepository.findAll().get(0);
    }
    @Transactional
    public void cambiarPorcentajeEspacio(Persona persona, UUID idEspacio,double porcentajeNuevo) throws Exception{
        //hacer el cambio
        Espacio espacio = espacioRepository.getById(idEspacio);
        if(espacio==null){
            throw new Exception("No existe el espacio con ese id");
        }
        espacio.modificarPorcentajeOcupacion(persona,porcentajeNuevo);
        espacioRepository.save(espacio);
        //Comprobamos las reservasVivas que tienen ese espacio asignado
        // se notifica las reservas que con el cambio no han podido cumplir los requisitos
        //comprobarReservasVivas(List.of(idEspacio));
    }

    public List<Espacio> obtenerEspaciosReservas(List<Reserva> reservasVivasPersona) {
        List<UUID> listId= new ArrayList<>();
        reservasVivasPersona.forEach(reserva -> listId.addAll(reserva.getIdEspacios()));
        return espacioRepository.findAllById(listId);
    }
    @Transactional
    public void comprobarDespachos(boolean rol_antiguo_asignable, Departamento departamentoAntiguo, Persona persona) throws Exception{
        //log.info(rol_antiguo+"pasa a "+persona.rolPrincipal());
        if(rol_antiguo_asignable && !persona.asignable()){ //los despachos ya no pueden estar asignados a esta persona
            List<Espacio> espaciosAfectados = todosEspacios().stream()
                    .filter(espacio -> espacio.getReservabilidad().categoriaReserva.equals(CategoriaReserva.DESPACHO)) //todos los despachos
                    .filter(espacio -> espacio.getPropietarioEspacio().esPersonas()) // los que tengan personas
                    .filter(espacio -> espacio.getPropietarioEspacio().personas.contains(persona.getIdPersona())).toList(); // que contengan como propietaria a esta persona
            espaciosAfectados.forEach(espacio -> {
                if(espacio.getPropietarioEspacio().personas.size()>1){ // quitamos a la persona de propietaria y listo
                    espacio.getPropietarioEspacio().personas.remove(persona.getIdPersona());
                }else{ //asignar todos los despachos que tenía al departamento al que pertenecia
                    try {
                        espacio.asignarAEspacio(new PropietarioEspacio(departamentoAntiguo));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                espacioRepository.save(espacio);
            });

        }
    }

    public void cambiarPropietarioEspacio(UUID idEspacio, PropietarioEspacio propietarioEspacio) throws Exception {
        Espacio espacio = espacioRepository.getById(idEspacio);
        if(espacio==null){
            throw new Exception("No existe el espacio con ese id");
        }
        espacio.asignarAEspacio(propietarioEspacio);
        espacioRepository.save(espacio);
    }
}
