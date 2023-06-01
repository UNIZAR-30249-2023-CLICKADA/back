package com.clickada.back.application;

import com.clickada.back.domain.PersonaRepository;
import com.clickada.back.domain.ReservaRepository;
import com.clickada.back.domain.entity.Espacio;
import com.clickada.back.domain.entity.Persona;
import com.clickada.back.domain.entity.Reserva;
import com.clickada.back.domain.entity.auxClasses.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Service
public class ReservaService {
    ReservaRepository reservaRepository;
    PersonaRepository personaRepository;
    EnviaMail servicioCorreo;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, PersonaRepository personaRepository,
                          EnviaMail servicioCorreo){
        this.reservaRepository = reservaRepository;
        this.personaRepository = personaRepository;
        this.servicioCorreo = servicioCorreo;
    }
    @Transactional (readOnly = true)
    public List<Reserva> listarTodasReservas(){
        return reservaRepository.findAll();
    }
    @Transactional(readOnly = true)
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

    public List<Reserva> reservasPorFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }

    public void reservar(Reserva reservaCompletada) {
        reservaRepository.save(reservaCompletada);
    }

    public List<Reserva> reservasVivasPersona(Persona gerente,Persona cambioRol) throws Exception {
        List<Reserva> reservasVivas = obtenerReservasVivas(gerente.getIdPersona());
        return reservasVivas.stream().filter(reserva -> reserva.getIdPersona().equals(cambioRol.getIdPersona())).toList();
    }
    public void comprobarReservas(Persona cambioRol,List<Reserva> reservasPersona,List<Espacio> espacioList) throws Exception {

        for(Reserva reserva: reservasPersona){
            for(UUID idEspacio: reserva.getIdEspacios()){
                Espacio espacio = espacioList.stream().filter(espacio1 -> espacio1.getIdEspacio().equals(idEspacio)).toList().get(0);
                if(!espacio.aptoCambioRol_Y_Reservabilidad(cambioRol)){
                    String mail = this.personaRepository.getById(reserva.getIdPersona()).getEMail();
                    String nombre = this.personaRepository.getById(reserva.getIdPersona()).getNombre();

                    Executors.newSingleThreadExecutor()
                            .execute(() -> servicioCorreo.enviarCorreo(mail,1,nombre,reserva.getFecha(),
                                    reserva.getPeriodoReserva().getHoraInicio()));

                    reservaRepository.delete(reserva);
                }
            }
        }
    }

    public List<Reserva> reservasVivasEspacios(List<UUID> idEspacios) throws Exception {
        List<Reserva> reservasVivas = new java.util.ArrayList<>(reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getFecha().isAfter(LocalDate.now())).toList());
        reservasVivas.addAll(reservaRepository.findByFecha(LocalDate.now())
                .stream().filter(reserva1 -> reserva1.getPeriodoReserva().getHoraFin().isAfter(LocalTime.now()))
                .toList());
        reservasVivas.stream().filter(reserva1 -> reserva1.getIdEspacios().stream()
                        .anyMatch(idEspacios::contains))
                .toList();
        return reservasVivas;
    }
    public void comprobarReservasEspacios(List<Reserva> reservasVivas,List<Espacio> espacioList, List<Persona> personasImplicadas) throws Exception {
        int totalAsistentesPermitidos;

        for(Reserva reserva  : reservasVivas){
            totalAsistentesPermitidos = 0;
            for(UUID idEspacio : reserva.getIdEspacios()){
                Espacio espacio = espacioList.stream()
                        .filter(espacio1 -> espacio1.getIdEspacio().equals(idEspacio)).toList().get(0);
                Persona personasReserva = personasImplicadas.stream()
                        .filter(persona -> persona.getIdPersona().equals(reserva.getIdPersona()))
                        .toList().get(0);
                totalAsistentesPermitidos+=espacio.getTotalAsistentesPermitidos();
                if(!espacio.aptoCambioRol_Y_Reservabilidad(personasReserva)){
                    enviarCorreo(personasImplicadas,reserva);
                    break;
                }
            }
            if(totalAsistentesPermitidos<reserva.getNumOcupantes()){
                enviarCorreo(personasImplicadas,reserva);
            }
        }
    }

    private void enviarCorreo( List<Persona> personasImplicadas, Reserva reserva){
        Persona persona = personasImplicadas.stream()
                .filter(persona1 -> persona1.getIdPersona().equals(reserva.getIdPersona())).toList().get(0);
        String mail = persona.getEMail();

        Executors.newSingleThreadExecutor()
                .execute(() -> servicioCorreo.enviarCorreo(mail,1,persona.getNombre(),reserva.getFecha(),
                        reserva.getPeriodoReserva().getHoraInicio()));

        reservaRepository.delete(reserva);
    }

    public void eliminarReserva(UUID idReserva, Persona gerente) throws Exception {
        if(!reservaRepository.existsById(idReserva)){
            throw new Exception("La reserva que se quiere eliminar no existe");
        }
        Reserva reserva = reservaRepository.getById(idReserva);
        reservaRepository.deleteById(idReserva);
        String mail = gerente.getEMail();

        Executors.newSingleThreadExecutor()
                .execute(() -> servicioCorreo.enviarCorreo(mail,2,gerente.getNombre(),reserva.getFecha(),
                        reserva.getPeriodoReserva().getHoraInicio()));
    }
}
